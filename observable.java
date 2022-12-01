import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.*;

interface observer {
    void notification();
}



interface subject {
    void register();
    void unregister(int id);
    void sendnotify();
}


class subscriber implements observer {
    private final int id;
    private final Date date;
    private boolean wantsub;
    subscriber(builder b){
        this.id=b.id;
        this.date=b.subdate;
        this.wantsub=b.wantsub;
    }
    @Override
    public void notification() {
        System.out.println("Some updates are there");
    }
    public static class builder {
        private int id;
        private Date subdate;
        private boolean wantsub;
        public static builder newinstance() {
            return new builder();
        }
        public builder setId(int id) {
            this.id=id;
            return this;
        }
        public builder setdate() {
            this.subdate=new Date();
            return this;
        }
        public builder setwill(boolean sub) {
            this.wantsub=sub;
            return this;
        }
        public subscriber build() {
            return new subscriber(this);
        }
    }
    @Override
    public String toString() {
        return this.id+" "+this.date;
    }
}

class subscriber_reciever {
    private volatile subscriber sub;
    private static int count=1;
    public subscriber_reciever(int numsub) {
      for(int i=0;i<numsub;i++){
        Thread t1=new Thread(){

            @Override
            public void run() {
                sub=subscriber.builder.newinstance().setId(count++).setdate().setwill(true).build();
            }
        };
        t1.start();
      }
    }

    public subscriber getsubscriber(){
        return sub;
    }
}
 

class publisher implements subject {

    private int regcnt;
    public publisher(int n) {
        this.regcnt=n;
    }
    private static  List<subscriber> subs=new ArrayList<>();

    @Override
    public void register() {
        subscriber_reciever sr=new subscriber_reciever(regcnt);
        subs.add(sr.getsubscriber());
        System.out.println(sr.getsubscriber());

    }

    @Override
    public void unregister(int id) {
        System.out.println("Subscriber 1 removed");
        subs.remove(0);
    }

    @Override
    public void sendnotify() {
        for(Iterator<subscriber> it=subs.iterator();it.hasNext();) {
            it.next().notification();
        }
    }
    public void datachanged() {
        sendnotify();
    }
}

public class observable {
    public static void main(String[] args) throws IOException {
        publisher p=new publisher(10);
        String oper;
        System.out.println("Operation: ");
        BufferedReader inp=new BufferedReader(new InputStreamReader(System.in));
      
        for(int i=0;i<=10;i++){
            oper=inp.readLine();
            switch(oper) {
            case "reg":
               p.register();
               break;
            case "unreg":
               p.unregister(0);
               break;
            case "not":
               p.datachanged();
               break;
            default:
               break;
        }
    }
        
    }
}