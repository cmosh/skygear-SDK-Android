/*
 * Copyright 2017 Oursky Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.skygear.skygear;

import android.app.Application;

/**
 * An extended application class for Skygear
 */
public abstract class SkygearApplication extends Application {
    /**
     * Gets Skygear endpoint.
     *
     * @return the Skygear endpoint
     */
    abstract public String getSkygearEndpoint();

    /**
     * Gets Skygear api key.
     *
     * @return the Skygear API key
     */
    abstract public String getApiKey();

    /**
     * Gets GCM Sender ID.
     *
     * @return the sender id
     */
    public String getGcmSenderId() {
        return null;
    }

    /**
     * Gets whether the PubsubClient Handler Execution Should be in Background.
     *
     * @return the boolean indicating whether the execution is in background
     */
    public boolean isPubsubHandlerExecutionInBackground() {
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Configuration config = new Configuration.Builder()
                .endPoint(this.getSkygearEndpoint())
                .apiKey(this.getApiKey())
                .gcmSenderId(this.getGcmSenderId())
                .pubsubHandlerExecutionInBackground(this.isPubsubHandlerExecutionInBackground())
                .build();

        Container.defaultContainer(this).configure(config);
    }
}
