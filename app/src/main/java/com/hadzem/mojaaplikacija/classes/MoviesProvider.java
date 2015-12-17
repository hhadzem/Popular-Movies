package com.hadzem.mojaaplikacija.classes;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.ProviGenOpenHelper;
import com.tjeannin.provigen.ProviGenProvider;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.Column.Type;
import com.tjeannin.provigen.annotation.ContentUri;

public class MoviesProvider extends ProviGenProvider {
    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new ProviGenOpenHelper(getContext(), "MoviesDatabase_test", null, 1, new Class[]{MoviesContract.class});
    }

    @Override
    public Class[] contractClasses() {
        return new Class[]{MoviesContract.class};
    }

    public interface MoviesContract extends ProviGenBaseContract{
        @Column(Column.Type.TEXT)
        public static final String TITLE = "Title";

        @Column(Column.Type.INTEGER)
        public static final String ID = "ID";

        @Column(Column.Type.TEXT)
        public static final String OVERVIEW = "Overview";

        @Column(Column.Type.TEXT)
        public static final String RELEASE_DATE ="ReleaseDate";

        @Column(Column.Type.REAL)
        public static final String POPULARITY = "Popularity";

        @Column(Column.Type.TEXT)
        public static final String IMAGE_URL_PATH = "ImagePath";

        @Column(Column.Type.TEXT)
        public static final String TRAILER_LINKS = "Trailers";

        @Column(Column.Type.TEXT)
        public static final String REVIEWS = "Reviews";
        @ContentUri
        public static final Uri CONTENT_URI = Uri.parse("content://com.hadzem.mojaaplikacija/tabela");
    }
}