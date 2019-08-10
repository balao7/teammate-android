/*
 * MIT License
 *
 * Copyright (c) 2019 Adetunji Dahunsi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.mainstreetcode.teammate.model

import android.annotation.SuppressLint
import android.os.Parcel


@SuppressLint("ParcelCreator")
object ModelStub : UserHost, TeamHost, Model<ModelStub> {
    override fun getImageUrl(): String = throw UnsupportedOperationException()

    override fun isEmpty(): Boolean = throw UnsupportedOperationException()

    override fun getId(): String = throw UnsupportedOperationException()

    override fun compareTo(other: ModelStub?): Int = throw UnsupportedOperationException()

    override fun writeToParcel(p0: Parcel?, p1: Int) = throw UnsupportedOperationException()

    override fun update(updated: ModelStub?) = throw UnsupportedOperationException()

    override fun describeContents(): Int = throw UnsupportedOperationException()

    override fun getUser(): User = User.empty()

    override fun getTeam(): Team = Team.empty()
}