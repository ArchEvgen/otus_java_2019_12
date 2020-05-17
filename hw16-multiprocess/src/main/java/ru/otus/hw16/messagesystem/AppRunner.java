package ru.otus.hw16.messagesystem;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.stereotype.Component;
import ru.otus.hw16.MessagingSystemDemo;

@Component
public class AppRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private String getJavaBin() {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        return javaBin;
    }

    private List<String> getRunCommand() {
        String path = AppRunner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.startsWith("file:")) {
            path = path.substring(5);
        }
        var till = path.indexOf('!');
        if (till > 0) {
            path = path.substring(0, till);
        }
        String classPath = ManagementFactory.getRuntimeMXBean().getClassPath();
        logger.info("AppRunner path: " + path);
        logger.info("AppRunner classPath: " + classPath);
        List<String> command = new ArrayList<>();
        command.add(getJavaBin());
        command.add("-classpath");
        command.add(classPath);
        if (path.endsWith(".jar")) {
            command.add("-jar");
            command.add(path);
        } else {
            command.add(MessagingSystemDemo.class.getName());
        }
        return command;
    }

    private void runFront(int port, int httpPort) throws IOException {
        var dir = System.getProperty("user.dir");
        logger.info("AppRunner dir: " + dir);
        var pb = new ProcessBuilder(getRunCommand())
                .inheritIO()
                .directory(new File(dir));
        pb.environment().put("message-system.port", String.valueOf(port));
        pb.environment().put("message-system.type", MessageSystemConfig.FRONT_TYPE);
        pb.environment().put("server.port", String.valueOf(httpPort));
        pb.environment().put("spring.main.web-application-type", WebApplicationType.SERVLET.name());
        pb.start();
    }

    private void runDb(int port) throws IOException {
        var dir = System.getProperty("user.dir");
        logger.info("AppRunner dir: " + dir);
        var pb = new ProcessBuilder(getRunCommand())
                .inheritIO()
                .directory(new File(dir));
        pb.environment().put("message-system.port", String.valueOf(port));
        pb.environment().put("message-system.type", MessageSystemConfig.DB_TYPE);
        pb.start();
    }

    public void runApp(MessageSystemConfigurationProperties properties) throws IOException {
        for (var p : properties.getDbPorts()) {
            runDb(p);
        }
        List<Integer> frontPorts = properties.getFrontPorts();
        List<Integer> frontHttpPorts = properties.getFrontHttpPorts();
        for (int i = 0; i < frontPorts.size(); i++) {
            Integer p = frontPorts.get(i);
            Integer hp = frontHttpPorts.get(i);
            runFront(p, hp);
        }
    }
}
