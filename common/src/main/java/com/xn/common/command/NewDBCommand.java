package com.xn.common.command;/**
 * Created by xn056839 on 2016/8/31.
 */

import com.xn.common.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewDBCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(NewDBCommand.class);

    @Override
    public void execute() {
        DBUtil.DBInit();
    }

    @Override
    public void executeWithException() throws Exception {

    }
}
