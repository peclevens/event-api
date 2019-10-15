package com.clivenspetit.events.usecase.session;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FindSessionUseCaseTest.class, FindSessionsUseCaseTest.class, CreateSessionUseCaseTest.class,
        UpdateSessionUseCaseTest.class, DeleteSessionUseCaseTest.class, DeleteSessionsUseCaseTest.class,
        UpvoteSessionUseCaseTest.class, DownvoteSessionUseCaseTest.class
})
public class SessionUseCaseTestSuite {

}
