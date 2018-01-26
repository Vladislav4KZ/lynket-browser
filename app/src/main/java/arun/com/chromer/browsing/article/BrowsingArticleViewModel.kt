/*
 * Chromer
 * Copyright (C) 2017 Arunkumar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package arun.com.chromer.browsing.article

import android.arch.lifecycle.ViewModel
import arun.com.chromer.data.Result
import arun.com.chromer.data.webarticle.WebArticleRepository
import arun.com.chromer.data.webarticle.model.WebArticle
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * A simple view model delivering a {@link Website} from repo.
 */
class BrowsingArticleViewModel
@Inject
constructor(private val webArticleRepository: WebArticleRepository) : ViewModel() {
    private val subs = CompositeSubscription()
    private val webSiteSubject = BehaviorSubject.create<Result<WebArticle>>(Result.Idle())

    fun loadWebSiteDetails(url: String): Observable<Result<WebArticle>> {
        if (webSiteSubject.value is Result.Idle<WebArticle>) {
            subs.add(webArticleRepository.getWebArticle(url)
                    .compose(Result.applyToObservable())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(webSiteSubject))
        }
        return webSiteSubject
    }

    override fun onCleared() {
        subs.clear()
    }
}
