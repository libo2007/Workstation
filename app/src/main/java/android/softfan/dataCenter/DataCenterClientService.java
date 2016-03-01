
package android.softfan.dataCenter;

import android.softfan.dataCenter.config.DataCenterClientConfig;
import android.softfan.util.systemUtil;
import android.softfan.util.wf_Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DataCenterClientService extends Thread {

    private static List<DataCenterClientService> clientServices = new ArrayList<DataCenterClientService>();

    public static DataCenterClientService startup(DataCenterClientConfig config) {
        synchronized (clientServices) {
            DataCenterClientService client = new DataCenterClientService(config);
            client.start();
            clientServices.add(client);
            wf_Log.sys_log("数据中心客户端(" + config.getAp() + "," + config.getOrg() + ")已经启动 ... OK");
            return client;
        }
    }

    public static void shutdown() {
        List<DataCenterClientService> tempclientServices;
        synchronized (clientServices) {
            tempclientServices = new ArrayList<DataCenterClientService>();
            tempclientServices.addAll(clientServices);
            clientServices.clear();
        }
        if (!tempclientServices.isEmpty()) {
            for (DataCenterClientService client : tempclientServices) {
                client.doDone();
                wf_Log.sys_log("数据中心客户端(" + client.getConfig().getAp() + "," + client.getConfig().getOrg() + ")已经关闭");
            }
            wf_Log.sys_log("");
        }
    }

    public static DataCenterClientService get(String ap, String org) {
        synchronized (clientServices) {
            if (!clientServices.isEmpty()) {
                for (DataCenterClientService client : clientServices) {
                    if (ap.equals(client.getApDataCenter().getApCode())) {
                        if (org.equals(client.getApDataCenter().getOrgId())) {
                            return client;
                        }
                    }
                }
            }
        }
        return null;
    }

    private DataCenterClientConfig config;

    private DataCenterClient client;

    private boolean done;

    private boolean finished;

    private ApDataCenter apDataCenter;

    private IDataCenterPushThread dataCenterPushThread;
    private IDataCenterProcess dataCenterProcess;

    public DataCenterClientService(DataCenterClientConfig config) {
        super("DataCenterClientService");
        this.config = config;
        this.client = null;
        this.dataCenterProcess = null;
        this.dataCenterPushThread = null;
        this.apDataCenter = new ApDataCenter(this.config.getAp(), this.config.getOrg(), this.config.getServerAp(), this.config.getServerOrg(), null);
    }

    public ApDataCenter getApDataCenter() {
        return apDataCenter;
    }

    public DataCenterClientConfig getConfig() {
        return config;
    }

    public void run() {
        try {
            finished = false;
            while (!done) {
                try {
                    try {
                        wf_Log.sys_log("连接到数据中心(" + this.config.getAddr() + ":" + this.config.getPort() + ")");
                        client = new DataCenterClient(this.config, getPushThreadService(), getProcessService(), apDataCenter, this.config.getAddr(), this.config.getPort());
                        client.run();
                    } finally {
                        client = null;
                    }
                } catch (DataCenterException e) {
                    wf_Log.sys_log(systemUtil.getErrorMessage(e));
                } catch (Exception e) {
                    wf_Log.sys_log(systemUtil.getErrorMessage(e));
                } catch (Throwable e) {
                    wf_Log.sys_log(systemUtil.getErrorMessage(e));
                }
                wf_Log.sys_log("断开数据中心(" + this.config.getAddr() + ":" + this.config.getPort() + ")");
                if (!done) {
                    synchronized (this) {
                        try {
                            this.wait(5000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        } finally {
            finished = true;
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    public void doDone() {
        synchronized (this) {
            done = true;
            this.notifyAll();
        }
        if (client != null) {
            client.doDone();
        }
        apDataCenter.notifySelfAll();
        while (!finished) {
            synchronized (this) {
                try {
                    this.wait(30000);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public boolean isDone() {
        return done;
    }

    private IDataCenterPushThread getPushThreadService() throws DataCenterException {
        if (dataCenterPushThread == null) {
            if (this.config.getPushThreadClass() != null) {
                try {
                    Class<?>[] params = new Class<?>[1];
                    params[0] = ApDataCenter.class;
                    Constructor<?> constructor = this.config.getPushThreadClass().getConstructor(params);
                    Object[] initargs = new Object[1];
                    initargs[0] = apDataCenter;
                    Object object = constructor.newInstance(initargs);
                    if (object instanceof IDataCenterPushThread) {
                        dataCenterPushThread = (IDataCenterPushThread) object;
                    }
                } catch (InstantiationException e) {
                    throw new DataCenterException(e);
                } catch (IllegalAccessException e) {
                    throw new DataCenterException(e);
                } catch (SecurityException e) {
                    throw new DataCenterException(e);
                } catch (NoSuchMethodException e) {
                    throw new DataCenterException(e);
                } catch (IllegalArgumentException e) {
                    throw new DataCenterException(e);
                } catch (InvocationTargetException e) {
                    throw new DataCenterException(e);
                }
            }
        }
        return dataCenterPushThread;
    }

    private IDataCenterProcess getProcessService() throws DataCenterException {
        if (dataCenterProcess == null) {
            if (this.config.getProcessClass() != null) {
                try {
                    Object object = this.config.getProcessClass().newInstance();
                    if (object instanceof IDataCenterProcess) {
                        dataCenterProcess = (IDataCenterProcess) object;
                    }
                } catch (InstantiationException e) {
                    throw new DataCenterException(e);
                } catch (IllegalAccessException e) {
                    throw new DataCenterException(e);
                }
            } else {
                dataCenterProcess = new DataCenterProcess();
            }
        }
        return dataCenterProcess;
    }

}