package com.xue.asyns;


import com.xue.bean.UserBase;
import com.xue.preference.PreferencesManager;

public class UserSaveTask extends SimpleAsyncTask<Void> {

    private UserBase userBase;

    public UserSaveTask(UserBase userBase) {
        this.userBase = userBase;
    }

    @Override
    public Void doInBackground() {
        PreferencesManager.get().setUser(userBase);
        return null;
    }

    @Override
    public void onPostExecute(Void result) {

    }
}
