package org.openehealth.ipf.commons.flow;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.search.event.impl.FullTextIndexEventListener;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.openehealth.ipf.commons.flow.hibernate.RenderedMessageDecryptEventListener;
import org.openehealth.ipf.commons.flow.hibernate.RenderedMessageEncryptEventListener;

public class CustomEventRegistration implements Integrator {

    @Override
    public void integrate(Configuration configuration, SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {
        EventListenerRegistry service = serviceRegistry.getService(org.hibernate.event.service.spi.EventListenerRegistry.class);
        
        StandardPBEStringEncryptor encrypt = new StandardPBEStringEncryptor();
        encrypt.setPassword("test_password");
        RenderedMessageEncryptEventListener encryptListener = new RenderedMessageEncryptEventListener();
        encryptListener.setStringEncryptor(encrypt);
        
        RenderedMessageDecryptEventListener decryptListener = new RenderedMessageDecryptEventListener();
        decryptListener.setStringEncryptor(encrypt);
        
        FullTextIndexEventListener fullTextListener = new FullTextIndexEventListener();

        service.appendListeners(EventType.PRE_UPDATE, encryptListener);
        service.prependListeners(EventType.POST_UPDATE, decryptListener);
        service.appendListeners(EventType.PRE_INSERT, encryptListener);
        service.prependListeners(EventType.POST_INSERT, decryptListener);
        service.appendListeners(EventType.POST_LOAD, decryptListener);
    }

    @Override
    public void integrate(MetadataImplementor metadata, SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {

    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        
    }

}
