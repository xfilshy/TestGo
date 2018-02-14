package com.xue.parsers;

import com.xue.bean.WalletInfo;

import org.json.JSONObject;

public class WalletInfoParser extends MasterParser<WalletInfo> {

    @Override
    public WalletInfo parse(JSONObject data) throws Exception {
        WalletInfo walletInfo = parseBase(optJSONObject(data, "wallet_info"));
        return walletInfo;
    }


    public WalletInfo parseBase(JSONObject data) throws Exception {
        WalletInfo walletInfo = null;

        if (data != null) {
            String id = optString(data, "id");
            String uid = optString(data, "uid");
            float diamond = optFloat(data, "diamond");
            float frozenDiamond = optFloat(data, "frozen_diamond");
            float ncoin = optFloat(data, "ncoin");
            float frozenNCcoin = optFloat(data, "frozen_ncoin");
            float enchashmentTotal = optFloat(data, "enchashment_count");
            float incomeNCoinTotal = optFloat(data, "income_ncoin_count");
            float money = optFloat(data, "money");

            if (diamond >= 0 && frozenDiamond >= 0 && ncoin >= 0 && frozenNCcoin >= 0 && enchashmentTotal >= 0 && incomeNCoinTotal >= 0 && money >= 0) {
                walletInfo = new WalletInfo();

                walletInfo.setId(id);
                walletInfo.setUid(uid);
                walletInfo.setDiamond(diamond);
                walletInfo.setFrozenDiamond(frozenDiamond);
                walletInfo.setNCoin(ncoin);
                walletInfo.setFrozenNCoin(frozenNCcoin);
                walletInfo.setEncashmentTotal(enchashmentTotal);
                walletInfo.setIncomeNCoinTotal(incomeNCoinTotal);
                walletInfo.setMoney(money);
            }
        }

        return walletInfo;
    }
}
