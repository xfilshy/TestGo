package com.xue.http.hook;

import com.xue.http.HttpLogTool;
import com.xue.http.exception.DataIsErrException;
import com.xue.http.exception.DataIsNullException;
import com.xue.http.exception.DataNoUpdateException;
import com.xue.http.exception.JsonCanNotParseException;
import com.xue.http.exception.ParseException;
import com.xue.http.exception.ResponseException;
import com.xue.http.impl.DataHull;
import com.xue.http.parse.BaseParser;

import java.io.IOException;

public abstract class HttpHandler<P extends BaseHttpParameter<?, B>, B> implements BaseHttpHandler<P, B> {

    @Override
    public DataHull<B> requestData(P httpParameter) {
        DataHull<B> dataHull;
        if (httpParameter == null) {
            dataHull = new DataHull<>();
            dataHull.setDataType(DataHull.DataType.PARAMS_IS_NULL);
            HttpLogTool.log("Parameter is null");
        } else {
            if (httpParameter.getType() == BaseHttpParameter.Type.GET || httpParameter.getType() == HttpParameter.Type.POST) {
                BaseParser<B, ?> parser = httpParameter.getParser();
                String response = null;
                dataHull = new DataHull<>();
                dataHull.setFunction(httpParameter.getBaseUrl());

                try {
                    if (httpParameter.getType() == BaseHttpParameter.Type.GET) {
                        response = doGet(httpParameter);
                    } else if (httpParameter.getType() == BaseHttpParameter.Type.POST) {
                        response = doPost(httpParameter);
                    }

                    HttpLogTool.log(response);
                    if (parser != null) {
                        dataHull.setDataEntity(parser.initialParse(response));
                        dataHull.setDataType(DataHull.DataType.DATA_IS_INTEGRITY);
                        HttpLogTool.log("complete!");

                        return dataHull;
                    } else {
                        dataHull.setDataType(DataHull.DataType.DATA_PARSER_IS_NULL);
                        HttpLogTool.log("Do not have parser");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    dataHull.setDataType(DataHull.DataType.CONNECTION_FAIL);
                    HttpLogTool.log("connected is fail");
                } catch (ResponseException e) {
                    e.printStackTrace();
                    dataHull.setDataType(DataHull.DataType.RESPONSE_CODE_ERR);
                    dataHull.setStatus(getResponseCode());
                    HttpLogTool.log("response is not 200");
                } catch (ParseException e) {
                    e.printStackTrace();
                    dataHull.setDataType(DataHull.DataType.DATA_PARSE_EXCEPTION);
                    HttpLogTool.log("parse error");
                } catch (DataIsNullException e) {
                    e.printStackTrace();
                    dataHull.setDataType(DataHull.DataType.DATA_IS_NULL);
                    HttpLogTool.log("data is null");
                } catch (JsonCanNotParseException e) {
                    e.printStackTrace();
                    dataHull.setDataType(DataHull.DataType.DATA_CAN_NOT_PARSE);
                    HttpLogTool.log("canParse is false");
                } catch (DataIsErrException e) {
                    e.printStackTrace();
                    dataHull.setDataType(DataHull.DataType.DATA_IS_ERR);
                    HttpLogTool.log("data is err");
                } catch (DataNoUpdateException e) {
                    e.printStackTrace();
                    dataHull.setDataType(DataHull.DataType.DATA_NO_UPDATE);
                    HttpLogTool.log("data has not update");
                } finally {
                    dataHull.setOriginalData(response);
                    if (dataHull.getStatus() == -2 && parser != null) {
                        dataHull.setMessage(parser.getMessage());
                        dataHull.setStatus(parser.getStatus());
                    }
                }
            } else {
                dataHull = new DataHull<>();
                dataHull.setDataType(DataHull.DataType.METHOD_IS_ERR);
                HttpLogTool.log("RequestMethod is error");
            }
        }
        dataHull.setDataId(httpParameter.getDataId());

        return dataHull;
    }
}