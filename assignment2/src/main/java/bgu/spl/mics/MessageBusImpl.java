package bgu.spl.mics;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static MessageBus instance = new MessageBusImpl();
	private ConcurrentHashMap<Class<? extends Message>, Queue<MicroService>> messgehash;
	private ConcurrentHashMap<MicroService, Queue<Message>> microQueue;
	//ConcurrentHashMap<Future<?>>;
	private MessageBusImpl() {
		//Initializing messgehash
		 messgehash = new ConcurrentHashMap<>();
		Queue<MicroService> q = new LinkedList<>();
		for (Class<? extends Message> mt:messgehash.keySet()) {
		messgehash.put(mt, q);
		}
		//Initializing microQueue
		 microQueue=new ConcurrentHashMap<>();
	}
	public static MessageBus getInstance() {
		return instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if(!messgehash.get(type).contains(m))
			messgehash.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (!messgehash.get(type).contains(m))
			messgehash.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		for (int i = 0; i < messgehash.get(b.getClass()).size(); i++) {
			MicroService temp = messgehash.get(b.getClass()).remove();
			microQueue.get(temp).add(b);
			messgehash.get(b.getClass()).add(temp);
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(MicroService m) {
		Queue<Message> q = new LinkedList<>();
	microQueue.put(m, q);
	}

	@Override
	public void unregister(MicroService m) {
		MicroService tempmicro;
		for (Class<? extends Message> mess:messgehash.keySet()){
			if (messgehash.get(mess).contains(m)){
				for (int i = 0; i <messgehash.get(mess).size() ; i++) {
					tempmicro=messgehash.get(mess).remove();
					if(tempmicro!=m)
						messgehash.get(mess).add(tempmicro);
				}
			}
		}
	microQueue.remove(m);//??
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
