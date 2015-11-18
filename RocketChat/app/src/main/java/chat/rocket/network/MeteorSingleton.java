package chat.rocket.network;

/**
 * Copyright 2014 www.delight.im <info@delight.im>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.LinkedList;
import java.util.List;

import de.tavendo.autobahn.WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification;

/**
 * Provides a single access point to the `Meteor` class that can be used across `Activity` instances
 */
public class MeteorSingleton extends Meteor implements MeteorCallback {

    private static final String TAG = "MeteorSingleton";
    private static MeteorSingleton mInstance;
    private final List<MeteorCallback> mCallbacks = new LinkedList<MeteorCallback>();

    private MeteorSingleton(final PersistenceHandler persistenceHandler, final String serverUri) {
        super(persistenceHandler, serverUri);
    }

    private MeteorSingleton(final PersistenceHandler persistenceHandler, final String serverUri, final String protocolVersion) {
        super(persistenceHandler, serverUri, protocolVersion);
    }

    public synchronized static MeteorSingleton createInstance(final PersistenceHandler persistenceHandler, final String serverUri) {
        return createInstance(persistenceHandler, serverUri, null);
    }

    public synchronized static MeteorSingleton createInstance(final PersistenceHandler persistenceHandler, final String serverUri, final String protocolVersion) {
        if (mInstance != null) {
            throw new RuntimeException("An instance has already been created");
        }

        if (protocolVersion == null) {
            mInstance = new MeteorSingleton(persistenceHandler, serverUri);
        } else {
            mInstance = new MeteorSingleton(persistenceHandler, serverUri, protocolVersion);
        }

        mInstance.mCallback = mInstance;

        return mInstance;
    }

    public synchronized static MeteorSingleton getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("Please call `createInstance(...)` first");
        }

        return mInstance;
    }

    public synchronized static boolean hasInstance() {
        return mInstance != null;
    }

    @Override
    public void setCallback(final MeteorCallback callback) {
        if (callback != null) {
            mCallbacks.add(callback);
        }
    }

    public void unsetCallback(final MeteorCallback callback) {
        if (callback != null) {
            mCallbacks.remove(callback);
        }
    }

    @Override
    public void onConnect(final boolean signedInAutomatically) {
        log(TAG);
        log("  onConnect");
        log("    signedInAutomatically == " + signedInAutomatically);

        for (MeteorCallback callback : mCallbacks) {
            if (callback != null) {
                callback.onConnect(signedInAutomatically);
            }
        }
    }

    @Override
    public void onDisconnect(final WebSocketCloseNotification code, final String reason) {
        log(TAG);
        log("  onDisconnect");
        log("    code == " + code);
        log("    reason == " + reason);

        for (MeteorCallback callback : mCallbacks) {
            if (callback != null) {
                callback.onDisconnect(code, reason);
            }
        }
    }

    @Override
    public void onDataAdded(final String collectionName, final String documentID, final String newValuesJson) {
        log(TAG);
        log("  onDataAdded");
        log("    collectionName == " + collectionName);
        log("    documentID == " + documentID);
        log("    newValuesJson == " + newValuesJson);

        for (MeteorCallback callback : mCallbacks) {
            if (callback != null) {
                callback.onDataAdded(collectionName, documentID, newValuesJson);
            }
        }
    }

    @Override
    public void onDataChanged(final String collectionName, final String documentID, final String updatedValuesJson, final String removedValuesJson) {
        log(TAG);
        log("  onDataChanged");
        log("    collectionName == " + collectionName);
        log("    documentID == " + documentID);
        log("    updatedValuesJson == " + updatedValuesJson);
        log("    removedValuesJson == " + removedValuesJson);

        for (MeteorCallback callback : mCallbacks) {
            if (callback != null) {
                callback.onDataChanged(collectionName, documentID, updatedValuesJson, removedValuesJson);
            }
        }
    }

    @Override
    public void onDataRemoved(final String collectionName, final String documentID) {
        log(TAG);
        log("  onDataRemoved");
        log("    collectionName == " + collectionName);
        log("    documentID == " + documentID);

        for (MeteorCallback callback : mCallbacks) {
            if (callback != null) {
                callback.onDataRemoved(collectionName, documentID);
            }
        }
    }

    @Override
    public void onException(final Exception e) {
        log(TAG);
        log("  onException");
        log("    e == " + e);

        for (MeteorCallback callback : mCallbacks) {
            if (callback != null) {
                callback.onException(e);
            }
        }
    }

}
