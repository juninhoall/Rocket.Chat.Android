package chat.rocket.app.db.collections;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.Loader;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import chat.rocket.app.db.DBContentProvider;
import chat.rocket.app.db.DBManager;
import chat.rocket.app.db.dao.CollectionDAO;
import chat.rocket.app.enumerations.ChannelType;
import chat.rocket.app.utils.Util;
import chat.rocket.models.TimeStamp;

/**
 * Created by julio on 24/11/15.
 */
//TODO: Implements Parceable
public class RCSubscription implements Serializable {
    public static final String COLLECTION_NAME = "rocketchat_subscription";

    private boolean alert;
    private TimeStamp ls;
    private String name;
    private boolean open;
    private String rid;
    //TODO: understand 't' field and make it an enum
    @SerializedName("t")
    private ChannelType type;
    private TimeStamp ts;
    private int unread;

    public boolean hasAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public TimeStamp getLs() {
        return ls;
    }

    public void setLs(TimeStamp ls) {
        this.ls = ls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public TimeStamp getTs() {
        return ts;
    }

    public void setTs(TimeStamp ts) {
        this.ts = ts;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public static List<RCSubscription> getRCSubscriptions() {
        List<CollectionDAO> daos = CollectionDAO.query(COLLECTION_NAME);
        List<RCSubscription> subs = new ArrayList<>();
        for (CollectionDAO dao : daos) {
            subs.add(Util.GSON.fromJson(dao.getNewValuesJson(), RCSubscription.class));
        }
        return subs;
    }


    public static Loader<Cursor> getLoader() {
        Uri uri = DBContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(CollectionDAO.TABLE_NAME).build();
        return DBManager.getInstance().getLoader(uri, null, CollectionDAO.COLUMN_COLLECTION_NAME + "=?", new String[]{RCSubscription.COLLECTION_NAME}, null);
    }
}