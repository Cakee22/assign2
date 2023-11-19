import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Set;

public class MBean {

    public static void main(String[] args) throws Exception {
        String objectName = "com.javacodegeeks.snippets.enterprise:type=Hello";
        QueryExp exp = Query.eq(Query.attr("Message"), Query.value("Hello World"));

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        // Construct the ObjectName for the Hello MBean we will register
        ObjectName mbeanName = new ObjectName(objectName);

        Hello mbean = new Hello();

        server.registerMBean(mbean, mbeanName);

        Set<ObjectInstance> instances = server.queryMBeans(new ObjectName(objectName), null);
        System.out.println(instances.size());

        for (int i = 0; i < instances.size(); i++) {
            ObjectInstance instance = (ObjectInstance) instances.toArray()[i];

            System.out.println("Class Name:t" + instance.getClassName());
            System.out.println("Object Name:t" + instance.getObjectName());
        }

    }

    static class Hello implements HelloMBean {

        private String message = "Hello World";

        @Override
        public String getMessage() {
            return this.message;
        }

        @Override
        public void sayHello() {
            System.out.println(message);
        }

        @Override
        public void setMessage(String message) {
            this.message = message;
        }

    }

    public static interface HelloMBean {

        // operations

        public void sayHello();

        // attributes

        // a read-write attribute called Message of type String
        public String getMessage();

        public void setMessage(String message);


    }
}