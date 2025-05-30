package org.cache2k.pinpoint.stress.pairwise;

/*
 * #%L
 * cache2k pinpoint
 * %%
 * Copyright (C) 2000 - 2020 headissue GmbH, Munich
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Jens Wilke
 */
public class ActorPairTest {

    @Test
    public void successFalseFalse() {
        ActorPair.SuccessTuple t = new ActorPair.SuccessTuple(false, false);
        assertFalse(t.isBothSucceed());
        assertFalse(t.isOneSucceeds());
        assertFalse(t.isSuccess1());
        assertFalse(t.isSuccess2());
        t.toString();
    }

    @Test
    public void successTrueFalse() {
        ActorPair.SuccessTuple t = new ActorPair.SuccessTuple(true, false);
        assertFalse(t.isBothSucceed());
        assertTrue(t.isOneSucceeds());
        assertTrue(t.isSuccess1());
        assertFalse(t.isSuccess2());
    }
}
