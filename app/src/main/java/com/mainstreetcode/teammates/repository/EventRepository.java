package com.mainstreetcode.teammates.repository;


import com.mainstreetcode.teammates.model.Event;
import com.mainstreetcode.teammates.model.Team;
import com.mainstreetcode.teammates.persistence.AppDatabase;
import com.mainstreetcode.teammates.persistence.EventDao;
import com.mainstreetcode.teammates.rest.TeammateApi;
import com.mainstreetcode.teammates.rest.TeammateService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;

import static io.reactivex.Single.just;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

public class EventRepository extends ModelRespository<Event> {

    private static EventRepository ourInstance;

    private final TeammateApi api;
    private final EventDao eventDao;
    private final ModelRespository<Team> teamRepository;

    private EventRepository() {
        api = TeammateService.getApiInstance();
        eventDao = AppDatabase.getInstance().eventDao();
        teamRepository = TeamRepository.getInstance();
    }

    public static EventRepository getInstance() {
        if (ourInstance == null) ourInstance = new EventRepository();
        return ourInstance;
    }

    @Override
    public Single<Event> createOrUpdate(Event event) {
        Single<Event> eventSingle = event.isEmpty()
                ? api.createEvent(event).map(localMapper(event))
                : api.updateEvent(event.getId(), event).map(localMapper(event));

        MultipartBody.Part body = getBody(event.get(Event.LOGO_POSITION).getValue(), Event.PHOTO_UPLOAD_KEY);
        if (body != null) {
            eventSingle = eventSingle.flatMap(put -> api.uploadEventPhoto(event.getId(), body));
        }

        return eventSingle.map(getSaveFunction()).observeOn(mainThread());
    }

    @Override
    public Flowable<Event> get(String id) {
        Maybe<Event> local = eventDao.get(id).subscribeOn(io());
        Maybe<Event> remote = api.getEvent(id).map(getSaveFunction()).toMaybe();

        return cacheThenRemote(local, remote);
    }

    @Override
    public Single<Event> delete(Event event) {
        return api.deleteEvent(event.getId())
                .flatMap(deleted -> {
                    eventDao.delete(event);
                    return just(event);
                });
    }

    public Flowable<List<Event>> getEvents(String userId) {
        Maybe<List<Event>> local = eventDao.getEvents(userId).subscribeOn(io());
        Maybe<List<Event>> remote = api.getEvents().map(getSaveManyFunction()).toMaybe();

        return cacheThenRemote(local, remote);
    }

    public Single<Event> rsvpEvent(final Event event, boolean attending) {
        return api.rsvpEvent(event.getId(), attending)
                .map(localMapper(event))
                .map(getSaveFunction())
                .observeOn(mainThread());
    }

    @Override
    Function<List<Event>, List<Event>> provideSaveManyFunction() {
        return models -> {
            List<Team> teams = new ArrayList<>(models.size());
            for (Event event : models) teams.add(event.getTeam());

            teamRepository.getSaveManyFunction().apply(teams);
            eventDao.upsert(Collections.unmodifiableList(models));

            return models;
        };
    }
}