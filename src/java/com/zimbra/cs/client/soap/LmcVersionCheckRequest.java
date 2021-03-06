/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2011, 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.cs.client.soap;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.util.Iterator;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.AdminConstants;
import com.zimbra.common.soap.DomUtil;
import com.zimbra.cs.versioncheck.VersionUpdate;
public class LmcVersionCheckRequest extends LmcSoapRequest {
	private String mAction;
    protected Element getRequestXML() {
        Element request = DocumentHelper.createElement(AdminConstants.VC_REQUEST);
        
        if(mAction == null) {
        	this.setAction(AdminConstants.VERSION_CHECK_CHECK);
        }
        addAttrNotNull(request, AdminConstants.E_ACTION, mAction);
        return request;
    }

    protected LmcSoapResponse parseResponseXML(Element responseXML) throws ServiceException {
    	LmcVersionCheckResponse response = new LmcVersionCheckResponse();
        if(mAction == AdminConstants.A_VERSION_CHECK_STATUS) {
	    	try {
	        	Element evc = DomUtil.get(responseXML, AdminConstants.E_VERSION_CHECK);
		        response.setStatus(DomUtil.getAttrBoolean(evc, AdminConstants.A_VERSION_CHECK_STATUS));
		        Element eUpdates = DomUtil.get(evc, AdminConstants.E_UPDATES);
		        for(Iterator<Element> iter = eUpdates.elementIterator();iter.hasNext();) {
		        	Element eUpdate = iter.next();
		        	VersionUpdate upd = new VersionUpdate();
		        	upd.setCritical(DomUtil.getAttrBoolean(eUpdate, AdminConstants.A_CRITICAL));
		        	upd.setType(DomUtil.getAttr(eUpdate, AdminConstants.A_UPDATE_TYPE));
		        	upd.setShortversion(DomUtil.getAttr(eUpdate, AdminConstants.A_SHORT_VERSION));
		        	upd.setRelease(DomUtil.getAttr(eUpdate, AdminConstants.A_RELEASE));
		        	upd.setVersion(DomUtil.getAttr(eUpdate, AdminConstants.A_VERSION));
		        	upd.setDescription(DomUtil.getAttr(eUpdate, AdminConstants.A_DESCRIPTION));
		        	upd.setPlatform(DomUtil.getAttr(eUpdate, AdminConstants.A_PLATFORM));
		        	upd.setBuildtype(DomUtil.getAttr(eUpdate, AdminConstants.A_BUILDTYPE));
		        	upd.setUpdateURL(DomUtil.getAttr(eUpdate, AdminConstants.A_UPDATE_URL));
		        	response.addUpdate(upd);
		        }
	    	} catch (ServiceException ex) {
	    		
	    	}
        }
        return response;    	
    }

	public String getAction() {
		return mAction;
	}

	public void setAction(String action) {
		mAction = action;
	}
}
