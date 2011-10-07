//package com.aimprosoft.wavilon.service.impl;
//
//import com.aimprosoft.wavilon.model.*;
//import com.aimprosoft.wavilon.service.*;
//import com.aimprosoft.wavilon.spring.ObjectFactory;
//
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.List;
//
//public class ForwardsCouchDBServiceImpl extends CouchDBService implements ForwardsCouchDBService {
//    private AgentDatabaseService agentService = ObjectFactory.getBean(AgentDatabaseService.class);
//    private QueueDatabaseService queueService = ObjectFactory.getBean(QueueDatabaseService.class);
//    private ExtensionDatabaseService extensionService = ObjectFactory.getBean(ExtensionDatabaseService.class);
//    private RecordingDatabaseService recordingService = ObjectFactory.getBean(RecordingDatabaseService.class);
//
//    public List<BaseModel> getAllForwards(Long userId, Long organizationId) throws IOException {
//        List<BaseModel> forwards = new LinkedList<BaseModel>();
//
//        for (Agent agent : agentService.getAllAgentsByUser(userId, organizationId)) {
//            forwards.add(agent);
//        }
//
//        for (Queue queue : queueService.getAllQueuesByUser(userId, organizationId)) {
//            forwards.add(queue);
//        }
//
//        for (Extension extension : extensionService.getAllUsersCouchModelToExtension(userId, organizationId)) {
//            forwards.add(extension);
//        }
//
//        for (Recording recording : recordingService.getAllRecordingsByUserId(userId, organizationId)) {
//            forwards.add(recording);
//        }
//
//        return forwards;
//    }
//}
