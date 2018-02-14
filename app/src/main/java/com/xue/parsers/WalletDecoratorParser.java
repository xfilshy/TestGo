package com.xue.parsers;

import com.xue.bean.RechargeList;
import com.xue.bean.RechargeNCoinList;
import com.xue.bean.WalletDecorator;
import com.xue.bean.WalletInfo;

import org.json.JSONObject;

public class WalletDecoratorParser extends MasterParser<WalletDecorator> {

    @Override
    public WalletDecorator parse(JSONObject data) throws Exception {
        WalletDecorator walletDecorator = null;
        WalletInfo walletInfo = new WalletInfoParser().parse(data);
        RechargeList rechargeList = new RechargeListParser().parse(data);
        RechargeNCoinList rechargeNCoinList = new RechargeNCoinListParser().parse(data);

        if (walletInfo != null && rechargeList != null && rechargeNCoinList != null) {
            walletDecorator = new WalletDecorator();

            walletDecorator.setWalletInfo(walletInfo);
            walletDecorator.setRechargeList(rechargeList);
            walletDecorator.setRechargeNCoinList(rechargeNCoinList);
        }

        return walletDecorator;
    }
}
