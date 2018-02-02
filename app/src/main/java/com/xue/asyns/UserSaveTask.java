package com.xue.asyns;


import com.xue.bean.User;
import com.xue.preference.PreferencesManager;

public class UserSaveTask extends SimpleAsyncTask<Void> {

    private User user;

    public UserSaveTask(User user) {
        this.user = user;
    }

    @Override
    public Void doInBackground() {
        PreferencesManager.get().setUser(user);
        return null;
    }

    @Override
    public void onPostExecute(Void result) {

    }
}
