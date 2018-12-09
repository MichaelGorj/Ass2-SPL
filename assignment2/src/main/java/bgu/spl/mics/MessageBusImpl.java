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
	private ConcurrentHashMap<Event<?>, Future<?>> eventFutHash;

	private MessageBusImpl() {
		//Initializing messgehash
		messgehash = new ConcurrentHashMap<>();
		Queue<MicroService> q = new LinkedList<>();
		for (Class<? extends Message> mt : messgehash.keySet()) {
			messgehash.put(mt, q);
		}
		//Initializing microQueue
		microQueue = new ConcurrentHashMap<>();
		eventFutHash=new ConcurrentHashMap<>();
	}

	public static MessageBus getInstance() {
		return instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if (!messgehash.get(type).contains(m))
			messgehash.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (!messgehash.get(type).contains(m))
			messgehash.get(type).add(m);
	}

	@Override
	public /*synchronized*/  <T> void  complete(Event<T> e, T result) {
		Future<T> tmp = (Future<T>) eventFutHash.get(e);
		tmp.resolve(result);

	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		if (b.getClass() != null) {
			for (int i = 0; i < messgehash.get(b.getClass()).size(); i++) {
				MicroService temp = messgehash.get(b.getClass()).remove();
				microQueue.get(temp).add(b);
				messgehash.get(b.getClass()).add(temp);
			}
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if (e.getClass() != null) {
			Future<T> temp = new Future<>();
			eventFutHash.put(e, temp);
			MicroService tempM = messgehash.get(e.getClass()).remove();//??
			microQueue.get(tempM).add(e);//??
			messgehash.get(e.getClass()).add(tempM);//??
			return temp;
		}
		notifyAll();///?
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
		for (Class<? extends Message> mess : messgehash.keySet()) {
			if (messgehash.get(mess).contains(m)) {
				for (int i = 0; i < messgehash.get(mess).size(); i++) {
					tempmicro = messgehash.get(mess).remove();
					if (tempmicro != m)
						messgehash.get(mess).add(tempmicro);
				}
			}
		}
		microQueue.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if (microQueue.get(m) == null) {
			throw new InterruptedException();
		} else {
			while (microQueue.get(m).isEmpty()) {
				m.wait();//??
			}
			Message tmp = microQueue.get(m).remove();
			return tmp;
		}
	}


}
