package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.Url;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class UrlTest {

    @Url(message = "Url should be valid and start with http or https.")
    private String link;

    public UrlTest() {

    }

    public UrlTest(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
