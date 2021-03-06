/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lishui.demo.base_ui.util;

import android.view.View;
import android.view.Window;

import java.util.Arrays;

/**
 * Utility class to manage various window flags to control system UI.
 */
public class SystemUiController {

    // Various UI states in increasing order of priority
    public static final int UI_STATE_BASE_WINDOW = 0;
    public static final int UI_STATE_ROOT_VIEW = 1;


    public static final int FLAG_LIGHT_NAV = 1 << 0;
    public static final int FLAG_DARK_NAV = 1 << 1;
    public static final int FLAG_LIGHT_STATUS = 1 << 2;
    public static final int FLAG_DARK_STATUS = 1 << 3;

    private final Window mWindow;
    private final int[] mStates = new int[2];

    public SystemUiController(Window window) {
        mWindow = window;
    }

    public void updateUiState(int uiState, boolean isLightBgBar) {
        updateUiState(uiState, isLightBgBar
                ? (FLAG_LIGHT_NAV | FLAG_LIGHT_STATUS) : (FLAG_DARK_NAV | FLAG_DARK_STATUS));
    }

    public void resetUiState(int uiState) {
        updateUiState(uiState, 0);
    }

    private void updateUiState(int uiState, int flags) {
        if (mStates[uiState] == flags) {
            return;
        }
        mStates[uiState] = flags;

        int oldFlags = mWindow.getDecorView().getSystemUiVisibility();
        // Apply the state flags in priority order
        int newFlags = oldFlags;
        for (int stateFlag : mStates) {
            if (UiUtils.ATLEAST_OREO) {
                if ((stateFlag & FLAG_LIGHT_NAV) != 0) {
                    newFlags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                } else if ((stateFlag & FLAG_DARK_NAV) != 0) {
                    newFlags &= ~(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            }

            if ((stateFlag & FLAG_LIGHT_STATUS) != 0) {
                newFlags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else if ((stateFlag & FLAG_DARK_STATUS) != 0) {
                newFlags &= ~(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        if (newFlags != oldFlags) {
            mWindow.getDecorView().setSystemUiVisibility(newFlags);
        }
    }

    @Override
    public String toString() {
        return "mStates=" + Arrays.toString(mStates);
    }
}
