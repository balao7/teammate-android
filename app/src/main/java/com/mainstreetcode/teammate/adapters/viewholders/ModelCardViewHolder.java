package com.mainstreetcode.teammate.adapters.viewholders;

import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mainstreetcode.teammate.R;
import com.mainstreetcode.teammate.model.RemoteImage;
import com.mainstreetcode.teammate.util.ViewHolderUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.tunjid.androidbootstrap.view.recyclerview.InteractiveAdapter;
import com.tunjid.androidbootstrap.view.recyclerview.InteractiveViewHolder;

import static com.mainstreetcode.teammate.util.ViewHolderUtil.THUMBNAIL_SIZE;


public class ModelCardViewHolder<H extends RemoteImage, T extends InteractiveAdapter.AdapterListener> extends InteractiveViewHolder<T> {

    protected H model;

    TextView title;
    TextView subtitle;
    public ImageView thumbnail;

    ModelCardViewHolder(View itemView, T adapterListener) {
        super(itemView, adapterListener);
        title = itemView.findViewById(R.id.item_title);
        subtitle = itemView.findViewById(R.id.item_subtitle);
        thumbnail = itemView.findViewById(R.id.thumbnail);

        ViewHolderUtil.updateForegroundDrawable(itemView);
        if (isThumbnail()) thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public void bind(H model) {
        this.model = model;
        String imageUrl = model.getImageUrl();

        if (TextUtils.isEmpty(imageUrl)) thumbnail.setImageResource(R.color.dark_grey);
        else load(imageUrl, thumbnail);
    }

    public ImageView getThumbnail() { return thumbnail; }

    public boolean isThumbnail() {return true;}

    public ModelCardViewHolder<H, T> withTitle(@StringRes int titleRes) {
        title.setText(titleRes);
        return this;
    }

    public ModelCardViewHolder<H, T> withSubTitle(@StringRes int subTitleRes) {
        subtitle.setText(subTitleRes);
        return this;
    }

    void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) this.title.setText(title);
    }

    void setSubTitle(CharSequence subTitle) {
        if (!TextUtils.isEmpty(subTitle)) this.subtitle.setText(subTitle);
    }

    void load(String imageUrl, ImageView destination) {
        RequestCreator creator = Picasso.with(itemView.getContext()).load(imageUrl);

        if (!isThumbnail()) creator.fit().centerCrop();
        else creator.placeholder(R.drawable.bg_image_placeholder)
                .resize(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                .centerInside();

        creator.into(destination);
    }
}
