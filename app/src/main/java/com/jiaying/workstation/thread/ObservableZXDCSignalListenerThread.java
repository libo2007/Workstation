package com.jiaying.workstation.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.softfan.dataCenter.DataCenterClientService;
import android.softfan.dataCenter.DataCenterException;
import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.IDataCenterProcess;
import android.softfan.dataCenter.config.DataCenterClientConfig;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.dataCenter.task.IDataCenterNotify;
import android.softfan.util.textUnit;
import android.util.Log;

import com.jiaying.workstation.utils.MyLog;

//import com.cylinder.www.env.Signal;
//import com.cylinder.www.env.net.FilterSignal;
//import com.cylinder.www.env.net.RecordState;
//import com.cylinder.www.env.person.businessobject.Donor;

/**
 * Created by hipilee on 2014/11/19.
 */
// Consider using AsyncTask or HandlerThread
public class ObservableZXDCSignalListenerThread extends Thread implements IDataCenterNotify, IDataCenterProcess {
	private static final String TAG = "ObservableZXDCSignalListenerThread";
	private ObservableHint observableHint;

	public Boolean getIsContinue() {
		return isContinue;
	}

	private Boolean isContinue = true;
	private String ap = "libo";
	private String org = "jiaying";
//	private RecordState recordState;
//	private RecoverState recoverState;
//	private FilterSignal filterSignal;
//	private CheckSignal checkSignal;

	private static DataCenterClientService clientService;

//	public ObservableZXDCSignalListenerThread(RecordState recordState, FilterSignal filterSignal) {
//		Log.e("camera", "ObservableZXDCSignalListenerThread constructor" + "construct");
//
//		this.observableHint = new ObservableHint();
//
//		this.recordState = recordState;
//		this.recoverState = new RecoverState();
//		this.filterSignal = filterSignal;
//		this.checkSignal = new CheckSignal(this.filterSignal);
//	}
public ObservableZXDCSignalListenerThread() {
	Log.e("camera", "ObservableZXDCSignalListenerThread constructor" + "construct");

	this.observableHint = new ObservableHint();

//	this.recoverState = new RecoverState();
}
	public void addObserver(Observer observer) {
		observableHint.addObserver(observer);
	}

	public void deleteObserver(Observer observer) {
		observableHint.deleteObserver(observer);
	}

//	public void notifyObservers(Signal signal) {
//		observableHint.notifyObservers(signal);
//	}

	public void setIsContinue(Boolean isContinue) {
		this.isContinue = isContinue;
	}

	@Override
	public void run() {
		super.run();

		// there must be a pause if without there will be something wrong.
//		recoverState.recover(recordState, observableHint);
		MyLog.e(TAG,TAG + " is run");
		clientService = DataCenterClientService.get(ap, org);
		if (clientService == null) {
			DataCenterClientConfig config = new DataCenterClientConfig();
			config.setAddr("jiaying.picp.net");
			config.setPort(30014);
			config.setAp(ap);
			config.setOrg(org);
			config.setPassword("123456");
			config.setServerAp("JzDataCenter");
			config.setServerOrg("*");
			config.setProcess(this);
			// config.setPushThreadClass(DataCenterClientTestService.class);

			DataCenterClientService.startup(config);

			clientService = DataCenterClientService.get(ap, org);

			if(clientService == null){
				MyLog.e(TAG,"clientService == null");
			}
		}

		while (isContinue) {
			//*************************************************************
			//            n++;
			//            if (1 == n && checkSignal.check(Signal.CONFIRM)) {
			//                Donor.setUserName(Integer.toString((new Random()).nextInt(100)));
			//
			//                dealSignal(Signal.CONFIRM);
			//
			//
			//            }
			//
			//            if (2 == n && checkSignal.check(Signal.PUNCTURE)) {
			////                try {
			////                    Thread.sleep(3000);
			////                } catch (InterruptedException e) {
			////                    e.printStackTrace();
			////                }
			//
			////                dealSignal(Signal.PUNCTURE);
			//
			//            }
			//
			//            if (3 == n && checkSignal.check(Signal.START)) {
			////                try {
			////                    Thread.sleep(3000);
			////                } catch (InterruptedException e) {
			////                    e.printStackTrace();
			////                }
			//                dealSignal(Signal.START);
			//            }
			//
			//            if (4 == n && checkSignal.check(Signal.FIST)) {
			//                try {
			//                    Thread.sleep(3000);
			//                } catch (InterruptedException e) {
			//                    e.printStackTrace();
			//                }
			//                dealSignal(Signal.FIST);
			//            }
			//
			//            if (20 == n && checkSignal.check(Signal.END)) {
			//
			//                dealSignal(Signal.END);
			//
			//            }

			//                *******************************

			synchronized (this) {
				try {
					this.wait(5000);
				} catch (InterruptedException e) {
				}
			}
		}

		finishReceivingSignal();
	}

	public synchronized void finishReceivingSignal() {
		Log.e("camera", " finish");
		notify();
	}

	public synchronized void commitSignal(Boolean isInitiative) {
		try {
			Log.e("camera", "waitToCommitSignal " + 1);

			wait();

			Log.e("camera", "waitToCommitSignal " + 2);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
		}

		// If we close the APP initiative,then reset the states.
//		if (isInitiative) {
//			recordState.reset();
//		}
//		recordState.commit();
	}

	private class ObservableHint extends Observable {
		private ArrayList<Observer> arrayListObserver;

		private ObservableHint() {
			arrayListObserver = new ArrayList<Observer>();
		}

		@Override
		public void addObserver(Observer observer) {
			super.addObserver(observer);
			arrayListObserver.add(observer);
		}

		@Override
		public synchronized void deleteObserver(Observer observer) {
			super.deleteObserver(observer);
			arrayListObserver.remove(observer);
		}

		@Override
		public void notifyObservers(Object data) {
			super.notifyObservers(data);
			for (Observer observer : arrayListObserver) {
				observer.update(observableHint, data);
			}
		}
	}

//	private void dealSignal(Signal signal) {
//		switch (signal) {
//
//		case CONFIRM:
//			observableHint.notifyObservers(Signal.CONFIRM);
//			break;
//
//		case PUNCTURE:
//			observableHint.notifyObservers(Signal.PUNCTURE);
//			break;
//
//		case START:
//			observableHint.notifyObservers(Signal.START);
//			break;
//
//		case FIST:
//			observableHint.notifyObservers(Signal.FIST);
//			break;
//
//		case END:
//			observableHint.notifyObservers(Signal.END);
//			break;
//
//		default:
//			break;
//
//		}
//	}

//	private class RecoverState {
//		public void recover(RecordState recordState, ObservableHint observerZXDCSignalHandler) {
//			if (!recordState.getEnd()) {
//				Log.e("camera", "recover " + true);
//
//				Boolean flag[] = new Boolean[3];
//				flag[0] = recordState.getConfirm();
//				//                flag[1] = recordState.getPuncture();
//				flag[2] = recordState.getStart();
//
//				if (flag[0]) {
//					dealSignal(Signal.CONFIRM);
//					Log.e("camera", "recover " + "confirm");
//					selfSleep(1000);
//					if (flag[2]) {
//						dealSignal(Signal.START);
//						Log.e("camera", "recover " + "puncture");
//					}
//				}
//			} else {
//				Log.e("camera", "recover else" + false);
//
//				recordState.reset();
//			}
//		}
//	}

//	private class CheckSignal {
//		private FilterSignal filterSignal;
//
//		public CheckSignal(FilterSignal filterSignal) {
//			this.filterSignal = filterSignal;
//		}
//
//		public Boolean check(Signal signal) {
//
//			switch (signal) {
//			case CONFIRM:
//				return filterSignal.checkConfirm();
//
//			case PUNCTURE:
//				return filterSignal.checkPuncture();
//
//			case START:
//				return filterSignal.checkStart();
//
//			case FIST:
//				return filterSignal.checkFist();
//
//			case END:
//				return filterSignal.checkEnd();
//			}
//			return false;
//		}
//	}

	public void selfSleep(long m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
		}
	}

	public void onSend(DataCenterTaskCmd selfCmd) throws DataCenterException {
	}

	public void onResponse(DataCenterTaskCmd selfCmd, DataCenterTaskCmd responseCmd) throws DataCenterException {
	}

	public void onFree(DataCenterTaskCmd selfCmd) {
	}

	public void onTimeout(DataCenterTaskCmd selfCmd) {
	}

	public void processMsg(DataCenterRun dataCenterRun, DataCenterTaskCmd cmd) throws DataCenterException {
		Log.e("camera", "processMsg:" + cmd.getCmd());
//		if ("confirm".equals(cmd.getCmd())) {
//			DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
//			retcmd.setSeq(cmd.getSeq());
//			retcmd.setCmd("response");
//
//			if (checkSignal.check(Signal.CONFIRM)) {
//				Donor donor = Donor.getInstance();
//
//				donor.setDonorID(textUnit.ObjToString(cmd.getValue("donor_id")));
//				donor.setUserName(textUnit.ObjToString(cmd.getValue("donor_name")));
//
//				dealSignal(Signal.CONFIRM);
//
//				HashMap<String, Object> values = new HashMap<String, Object>();
//				values.put("ok", "true");
//				retcmd.setValues(values);
//
//				Log.e("camera", "CONFIRM");
//			}
//
//			dataCenterRun.sendResponseCmd(retcmd);
//		} else if ("start".equals(cmd.getCmd())) {
//			DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
//			retcmd.setSeq(cmd.getSeq());
//			retcmd.setCmd("response");
//
//			if (checkSignal.check(Signal.START)) {
//				dealSignal(Signal.START);
//
//				HashMap<String, Object> values = new HashMap<String, Object>();
//				values.put("ok", "true");
//				retcmd.setValues(values);
//
//				Log.e("camera", "START");
//			}
//
//			dataCenterRun.sendResponseCmd(retcmd);
//		} else if ("end".equals(cmd.getCmd())) {
//			DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
//			retcmd.setSeq(cmd.getSeq());
//			retcmd.setCmd("response");
//
//			if (checkSignal.check(Signal.END)) {
//				dealSignal(Signal.END);
//
//				HashMap<String, Object> values = new HashMap<String, Object>();
//				values.put("ok", "true");
//				retcmd.setValues(values);
//
//				Log.e("camera", "END");
//			}
//
//			dataCenterRun.sendResponseCmd(retcmd);
//		}
	}

	@Override
	public void onSended(DataCenterTaskCmd selfCmd) throws DataCenterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendTimeout(DataCenterTaskCmd selfCmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponseTimeout(DataCenterTaskCmd selfCmd) {
		// TODO Auto-generated method stub

	}

	public void startMsgProcess() {
	}

	public void stopMsgProcess() {
	}

	public static DataCenterClientService getClientService() {

		return clientService;
	}

}
