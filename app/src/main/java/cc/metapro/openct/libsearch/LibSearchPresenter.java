package cc.metapro.openct.libsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.metapro.openct.data.BookInfo;
import cc.metapro.openct.data.source.Loader;
import cc.metapro.openct.utils.Constants;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class LibSearchPresenter implements LibSearchContract.Presenter {

    private LibSearchContract.View mLibSearchView;

    private Spinner mSpinner;

    private EditText mEditText;

    private Context mContext;

    LibSearchPresenter(@NonNull LibSearchContract.View libSearchView, Spinner spinner, EditText editText) {
        mLibSearchView = libSearchView;
        mSpinner = spinner;
        mEditText = editText;
        mContext = mEditText.getContext();
        mLibSearchView.setPresenter(this);
    }

    @Override
    public void search() {
        mLibSearchView.showOnSearching();
        Observable
                .create(new ObservableOnSubscribe<List<BookInfo>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<BookInfo>> e) throws Exception {
                        Map<String, String> map = new HashMap<>(2);
                        map.put(Constants.SEARCH_TYPE, mSpinner.getSelectedItem().toString());
                        map.put(Constants.SEARCH_CONTENT, mEditText.getText().toString());
                        e.onNext(Loader.getLibrary().search(map));
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<BookInfo>>() {
                    @Override
                    public void accept(List<BookInfo> infos) throws Exception {
                        mLibSearchView.onSearchResult(infos);
                    }
                })
                .onErrorReturn(new Function<Throwable, List<BookInfo>>() {
                    @Override
                    public List<BookInfo> apply(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        mLibSearchView.onSearchResult(new ArrayList<BookInfo>(0));
                        return new ArrayList<>();
                    }
                })
                .subscribe();

    }

    @Override
    public void nextPage() {
        mLibSearchView.showOnSearching();
        Observable
                .create(new ObservableOnSubscribe<List<BookInfo>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<BookInfo>> e) throws Exception {
                        List<BookInfo> infos = Loader.getLibrary().getNextPage();
                        e.onNext(infos);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<BookInfo>>() {
                    @Override
                    public void accept(List<BookInfo> infos) throws Exception {
                        mLibSearchView.onNextPageResult(infos);
                    }
                })
                .onErrorReturn(new Function<Throwable, List<BookInfo>>() {
                    @Override
                    public List<BookInfo> apply(Throwable throwable) throws Exception {
                        Toast.makeText(mEditText.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        mLibSearchView.onNextPageResult(new ArrayList<BookInfo>());
                        return new ArrayList<>();
                    }
                })
                .subscribe();
    }

    @Override
    public void start() {

    }
}
