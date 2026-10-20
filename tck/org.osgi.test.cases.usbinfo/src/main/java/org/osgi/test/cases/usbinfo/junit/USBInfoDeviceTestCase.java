/*******************************************************************************
 * Copyright (c) Contributors to the Eclipse Foundation
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
 * SPDX-License-Identifier: Apache-2.0 
 *******************************************************************************/

package org.osgi.test.cases.usbinfo.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.service.usbinfo.USBInfoDevice;
import org.osgi.test.cases.usbinfo.util.TestServiceListener;
import org.osgi.test.cases.usbinfo.util.USBTestProxy;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.tck.step.TestStepProxy;

public class USBInfoDeviceTestCase {

	@InjectBundleContext
	BundleContext		context;
    private USBTestProxy	testProxy;

    @BeforeEach
    public void setUp() throws Exception {
        this.testProxy = new USBTestProxy(new TestStepProxy(context));
    }

    @AfterEach
    public void tearDown() throws Exception {
        this.testProxy.close();
    }

    /**
     * Tests register a USBInfoDevice service.
     */
    @Test
    public void testRegisterDevice01() {
        String[] ids = null;
        try {
			TestServiceListener<USBInfoDevice> listener = new TestServiceListener<>(
					ServiceEvent.REGISTERED);
            context.addServiceListener(listener, "(objectClass=org.osgi.service.usbinfo.USBInfoDevice)");

            String command = "registerDevice";
            String message = "[TEST-RD01] Connect a device.";
            ids = testProxy.executeTestStep(command, message, new String[]{});

            context.removeServiceListener(listener);

            assertThat(listener.size()).as("Only one USBInfoDevice service event is expected.").isOne();
			ServiceReference<USBInfoDevice> ref = listener.get(0);
            String[] category = (String[]) ref.getProperty(org.osgi.service.device.Constants.DEVICE_CATEGORY);
            boolean categoryFlag = false;
            for (int i = 0; i < category.length; i++) {
                if (category[i].equals(USBInfoDevice.DEVICE_CATEGORY)) {
                    categoryFlag = true;
                    break;
                }
            }
            assertThat(categoryFlag).as("The following service property is not correct: " + org.osgi.service.device.Constants.DEVICE_CATEGORY).isTrue();

            // usbinfo.bDeviceClass: String, Hexadecimal, 2-digits.
            Object value = ref.getProperty(USBInfoDevice.USB_BDEVICECLASS);
            assertThat(value instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICECLASS).isTrue();
            assertThat(((String) value)).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICECLASS).hasSize(2);
            try {
                Integer.parseInt(((String) value), 16);
            } catch (NumberFormatException e) {
                fail("The following service property is not correct: " + USBInfoDevice.USB_BDEVICECLASS, e);
            }

            // usbinfo.bDeviceSubClass: String, Hexadecimal, 2-digits.
            value = ref.getProperty(USBInfoDevice.USB_BDEVICESUBCLASS);
            assertThat(value instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICESUBCLASS).isTrue();
            assertThat(((String) value)).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICESUBCLASS).hasSize(2);
            try {
                Integer.parseInt(((String) value), 16);
            } catch (NumberFormatException e) {
                fail("The following service property is not correct: " + USBInfoDevice.USB_BDEVICESUBCLASS, e);
            }

            // usbinfo.bDeviceProtocol: String, Hexadecimal, 2-digits.
            value = ref.getProperty(USBInfoDevice.USB_BDEVICEPROTOCOL);
            assertThat(value instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICEPROTOCOL).isTrue();
            assertThat(((String) value)).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICEPROTOCOL).hasSize(2);
            try {
                Integer.parseInt(((String) value), 16);
            } catch (NumberFormatException e) {
                fail("The following service property is not correct: " + USBInfoDevice.USB_BDEVICEPROTOCOL, e);
            }

            // usbinfo.idVendor: String, Hexadecimal, 4-digits.
            value = ref.getProperty(USBInfoDevice.USB_IDVENDOR);
            assertThat(value instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_IDVENDOR).isTrue();
            assertThat(((String) value)).as("The following service property is not correct: " + USBInfoDevice.USB_IDVENDOR).hasSize(4);
            try {
                Integer.parseInt(((String) value), 16);
            } catch (NumberFormatException e) {
            	fail("The following service property is not correct: " + USBInfoDevice.USB_IDVENDOR, e);
            }

            // usbinfo.idProduct: String, Hexadecimal, 4-digits.
            value = ref.getProperty(USBInfoDevice.USB_IDPRODUCT);
            assertThat(value instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_IDPRODUCT).isTrue();
            assertThat(((String) value)).as("The following service property is not correct: " + USBInfoDevice.USB_IDPRODUCT).hasSize(4);
            try {
                Integer.parseInt(((String) value), 16);
            } catch (NumberFormatException e) {
                fail("The following service property is not correct: " + USBInfoDevice.USB_IDPRODUCT, e);
            }

            // usbinfo.bcdDevice: String, BCD format, 4-digits.
            value = ref.getProperty(USBInfoDevice.USB_BCDDEVICE);
            assertThat(value instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BCDDEVICE).isTrue();
            assertThat(((String) value)).as("The following service property is not correct: " + USBInfoDevice.USB_BCDDEVICE).hasSize(4);
            try {
                Integer.parseInt(((String) value), 10);
            } catch (NumberFormatException e) {
                fail("The following service property is not correct: " + USBInfoDevice.USB_BCDDEVICE, e);
            }

            // usbinfo.bInterfaceNumber: Integer.
            value = ref.getProperty(USBInfoDevice.USB_BINTERFACENUMBER);
            assertThat(value instanceof Integer).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACENUMBER).isTrue();

            // usbinfo.bInterfaceClass: String, Hexadecimal, 2-digits.
            value = ref.getProperty(USBInfoDevice.USB_BINTERFACECLASS);
            assertThat(value instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACECLASS).isTrue();
            assertThat(((String) value)).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACECLASS).hasSize(2);
            try {
                Integer.parseInt(((String) value), 16);
            } catch (NumberFormatException e) {
                fail("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACECLASS, e);
            }

            // usbinfo.bInterfaceSubClass: String, Hexadecimal, 2-digits.
            value = ref.getProperty(USBInfoDevice.USB_BINTERFACESUBCLASS);
            assertThat(value instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACESUBCLASS).isTrue();
            assertThat(((String) value)).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACESUBCLASS).hasSize(2);
            try {
                Integer.parseInt(((String) value), 16);
            } catch (NumberFormatException e) {
                fail("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACESUBCLASS, e);
            }

            // usbinfo.bInterfaceProtocol: String, Hexadecimal, 2-digits.
            value = ref.getProperty(USBInfoDevice.USB_BINTERFACEPROTOCOL);
            assertThat(value instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACEPROTOCOL).isTrue();
            assertThat(((String) value)).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACEPROTOCOL).hasSize(2);
            try {
                Integer.parseInt(((String) value), 16);
            } catch (NumberFormatException e) {
                fail("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACEPROTOCOL, e);
            }

            // usbinfo.bus: Integer.
            value = ref.getProperty(USBInfoDevice.USB_BUS);
            assertThat(value instanceof Integer).as("The following service property is not correct: " + USBInfoDevice.USB_BUS).isTrue();

            // usbinfo.address: Integer(001-127).
            value = ref.getProperty(USBInfoDevice.USB_ADDRESS);
            assertThat(value instanceof Integer).as("The following service property is not correct: " + USBInfoDevice.USB_ADDRESS).isTrue();
            assertThat(((Integer) value).intValue() >= 1).as("The following service property is not correct: " + USBInfoDevice.USB_ADDRESS).isTrue();
            assertThat(((Integer) value).intValue() <= 127).as("The following service property is not correct: " + USBInfoDevice.USB_ADDRESS).isTrue();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage(), e);
        } finally {
            if (ids != null) {
                testProxy.executeTestStep("unregisterDevice", "Disconnect the remaining device.", new String[] {ids[0]});
            }
        }
    }

    /**
     * Tests register two USBInfoDevice services.
     */
    @Test
    public void testRegisterDevice02() {
        String[] ids = null;
        String[] ids2 = null;
        try {
			TestServiceListener<USBInfoDevice> listener = new TestServiceListener<>(
					ServiceEvent.REGISTERED);
            context.addServiceListener(listener, "(objectClass=org.osgi.service.usbinfo.USBInfoDevice)");

            String command = "registerDevice";
            String message = "[TEST-RD02] Connect the first device.";
            ids = testProxy.executeTestStep(command, message, new String[]{});

            String message2 = "Connect the second device.";
            ids2 = testProxy.executeTestStep(command, message2, new String[]{});

            context.removeServiceListener(listener);

            assertThat(listener.size()).as("Two USBInfoDevice service events are expected.").isEqualTo(2);
			ServiceReference<USBInfoDevice> ref = listener.get(0);
            String[] category = (String[]) ref.getProperty(org.osgi.service.device.Constants.DEVICE_CATEGORY);
            boolean categoryFlag = false;
            for (int i = 0; i < category.length; i++) {
                if (category[i].equals(USBInfoDevice.DEVICE_CATEGORY)) {
                    categoryFlag = true;
                    break;
                }
            }
            assertThat(categoryFlag).as("The following service property is not correct: " + org.osgi.service.device.Constants.DEVICE_CATEGORY).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BDEVICECLASS) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICECLASS).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BDEVICESUBCLASS) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICESUBCLASS).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BDEVICEPROTOCOL) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICEPROTOCOL).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_IDVENDOR) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_IDVENDOR).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_IDPRODUCT) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_IDPRODUCT).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BCDDEVICE) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BCDDEVICE).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BINTERFACENUMBER) instanceof Integer).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACENUMBER).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BINTERFACECLASS) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACECLASS).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BINTERFACESUBCLASS) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACESUBCLASS).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BINTERFACEPROTOCOL) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACEPROTOCOL).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BUS) instanceof Integer).as("The following service property is not correct: " + USBInfoDevice.USB_BUS).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_ADDRESS) instanceof Integer).as("The following service property is not correct: " + USBInfoDevice.USB_ADDRESS).isTrue();


            ref = listener.get(1);
            category = (String[]) ref.getProperty(org.osgi.service.device.Constants.DEVICE_CATEGORY);
            categoryFlag = false;
            for (int i = 0; i < category.length; i++) {
                if (category[i].equals(USBInfoDevice.DEVICE_CATEGORY)) {
                    categoryFlag = true;
                    break;
                }
            }
            assertThat(categoryFlag).as("The following service property is not correct: " + org.osgi.service.device.Constants.DEVICE_CATEGORY).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BDEVICECLASS) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICECLASS).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BDEVICESUBCLASS) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICESUBCLASS).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BDEVICEPROTOCOL) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BDEVICEPROTOCOL).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_IDVENDOR) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_IDVENDOR).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_IDPRODUCT) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_IDPRODUCT).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BCDDEVICE) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BCDDEVICE).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BINTERFACENUMBER) instanceof Integer).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACENUMBER).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BINTERFACECLASS) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACECLASS).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BINTERFACESUBCLASS) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACESUBCLASS).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BINTERFACEPROTOCOL) instanceof String).as("The following service property is not correct: " + USBInfoDevice.USB_BINTERFACEPROTOCOL).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_BUS) instanceof Integer).as("The following service property is not correct: " + USBInfoDevice.USB_BUS).isTrue();
            assertThat(ref.getProperty(USBInfoDevice.USB_ADDRESS) instanceof Integer).as("The following service property is not correct: " + USBInfoDevice.USB_ADDRESS).isTrue();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage(), e);
        } finally {
            if (ids != null) {
                testProxy.executeTestStep("unregisterDevice", "Discconect the remaining device.", new String[] {ids[0]});
            }
            if (ids2 != null) {
                testProxy.executeTestStep("unregisterDevice", "Discconect the remaining device.", new String[] {ids2[0]});
            }
        }
    }

    /**
     * Tests unregister a USBInfoDevice service.
     */
    @Test
    public void testUnregisterDevice01() {
        String[] ids = null;
        try {
            String command = "registerDevice";
            String message = "[TEST-UD01] Connect a device.";
            ids = testProxy.executeTestStep(command, message, new String[]{});

			TestServiceListener<USBInfoDevice> listener = new TestServiceListener<>(
					ServiceEvent.UNREGISTERING);
            context.addServiceListener(listener, "(objectClass=org.osgi.service.usbinfo.USBInfoDevice)");

            command = "unregisterDevice";
            message = "Disonnect the device.";
            testProxy.executeTestStep(command, message, new String[] {ids[0]});

            context.removeServiceListener(listener);

            assertThat(listener.size()).as("Only one USBInfoDevice service event is expected.").isOne();
            Long registerId = Long.valueOf(ids[0]);
            Long unregisterId = (Long) listener.get(0).getProperty(Constants.SERVICE_ID);
            assertThat(unregisterId).as("The unregisterd service is not correct.").isEqualTo(registerId);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage(), e);
        }
    }

    /**
     * Tests unregister a USBInfoDevice service. 1. register A 2. register B 3.
     * unregister A
     */
    @Test
    public void testUnregisterDevice02() {
        String[] ids = null;
        String[] ids2 = null;
        try {
            String command = "registerDevice";
            String message = "[TEST-UD02] Connect the first device.";
            ids = testProxy.executeTestStep(command, message, new String[]{});

            message = "Connect the second device.";
            ids2 = testProxy.executeTestStep(command, message, new String[]{});

			TestServiceListener<USBInfoDevice> listener = new TestServiceListener<>(
					ServiceEvent.UNREGISTERING);
            context.addServiceListener(listener, "(objectClass=org.osgi.service.usbinfo.USBInfoDevice)");

            command = "unregisterDevice";
            message = "Disconnect the first device.";
            testProxy.executeTestStep(command, message, new String[] {ids[0]});

            context.removeServiceListener(listener);

            assertThat(listener.size()).as("Only one USBInfoDevice service event is expected.").isOne();
            Long registerId = Long.valueOf(ids[0]);
            Long unregisterId = (Long) listener.get(0).getProperty(Constants.SERVICE_ID);
            assertThat(unregisterId).as("The unregisterd service is not correct.").isEqualTo(registerId);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage(), e);
        } finally {
            if (ids2 != null) {
                testProxy.executeTestStep("unregisterDevice", "Discconect the remaining device.", new String[] {ids2[0]});
            }
        }
    }

    /**
     * Tests unregister a USBInfoDevice service. 1. register A 2. register B 3.
     * unregister A 4. unregister B
     */
    @Test
    public void testUnregisterDevice03() {
        String[] ids = null;
        String[] ids2 = null;
        try {
            String command = "registerDevice";
            String message = "[TEST-UD03] Connect the first device.";
            ids = testProxy.executeTestStep(command, message, new String[]{});

            message = "Connect the second device.";
            ids2 = testProxy.executeTestStep(command, message, new String[]{});

			TestServiceListener<USBInfoDevice> listener = new TestServiceListener<>(
					ServiceEvent.UNREGISTERING);
            context.addServiceListener(listener, "(objectClass=org.osgi.service.usbinfo.USBInfoDevice)");

            command = "unregisterDevice";
            message = "Disconnect the first device.";
            testProxy.executeTestStep(command, message, new String[] {ids[0]});

            message = "Disconnect the second device.";
            testProxy.executeTestStep(command, message, new String[] {ids2[0]});

            context.removeServiceListener(listener);

            assertThat(listener.size()).as("Only two USBInfoDevice service events are expected.").isEqualTo(2);
            Long registerId = Long.valueOf(ids[0]);
            Long unregisterId = (Long) listener.get(0).getProperty(Constants.SERVICE_ID);
            assertThat(unregisterId).as("The unregister service is not correct.").isEqualTo(registerId);
            registerId = Long.valueOf(ids2[0]);
            unregisterId = (Long) listener.get(1).getProperty(Constants.SERVICE_ID);
            assertThat(unregisterId).as("The unregister service is not correct.").isEqualTo(registerId);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage(), e);
        }
    }
}
