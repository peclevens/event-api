package com.clivenspetit.events.usecase.event;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Clivens Petit
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FindEventUseCaseTest.class, FindEventsUseCaseTest.class, CreateEventUseCaseTest.class,
        UpdateEventUseCaseTest.class, DeleteEventUseCaseTest.class, DeleteEventsUseCaseTest.class
})
public class EventUseCaseTestSuite {

}
