package org.hermes.core.research;

import org.hermes.core.exceptions.ResearchException;

/**
 * Created by Almaz
 * Date: 13.10.2015
 */
public interface Research {

    ResearchResult research() throws ResearchException;

}
