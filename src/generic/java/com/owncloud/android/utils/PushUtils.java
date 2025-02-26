/**
 * Nextcloud Android client application
 *
 * @author Mario Danic
 * @author Chris Narkiewicz
 * Copyright (C) 2017 Mario Danic
 * Copyright (C) 2019 Chris Narkiewicz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.utils;

import android.content.Context;

import com.amperbackup.client.account.UserAccountManager;
import com.owncloud.android.MainApp;
import com.owncloud.android.datamodel.SignatureVerification;
import com.amperbackup.client.preferences.AppPreferencesImpl;

import java.security.Key;

public final class PushUtils {
    public static final String KEY_PUSH = "push";

    private PushUtils() {
    }

    public static void pushRegistrationToServer(final UserAccountManager accountManager, final String pushToken) {
        // do nothing
    }

    public static void reinitKeys(UserAccountManager accountManager) {
        Context context = MainApp.getAppContext();
        AppPreferencesImpl.fromContext(context).setKeysReInitEnabled();
    }

    public static Key readKeyFromFile(boolean readPublicKey) {
        return null;
    }

    public static SignatureVerification verifySignature(
        final Context context,
        final UserAccountManager accountManager,
        final byte[] signatureBytes,
        final byte[] subjectBytes
    ) {
        return null;
    }

}
