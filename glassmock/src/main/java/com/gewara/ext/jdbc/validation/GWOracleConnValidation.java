/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.gewara.ext.jdbc.validation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provide custom implementation of connection validation for oracle dbvendor.
 * 
 * Provides a custom connection validation mechanism for Oracle dbVendor if
 * custom-validation is chosen as the connection-validation-method.
 * 
 * @author Shalini M
 */
public class GWOracleConnValidation {
	private static long endtime = 0;
	private static boolean start = false;
	private static final String SQL = "select 1 from dual";
	private static int invalidTimes;
	private static int validateTimes;
	private static int tryCount = 0;

	public static void enableValidate(long time){
		endtime = time;
		start = true;
	}
	public static void enableValidateByTryCount(int count){
		start = true;
		tryCount = count;
	}
	public static void disableValidate(){
		start=false;
		tryCount = 0;
	}

	/**
	 * Check for validity of <code>java.sql.Connection</code>
	 * 
	 * @param con
	 *           <code>java.sql.Connection</code>to be validated
	 * @throws SQLException
	 *            if the connection is not valid
	 */
	public boolean isConnectionValid(Connection con) {
		if(start){
			if(System.currentTimeMillis() < endtime || tryCount > 0){
				tryCount --;
				validateTimes ++;
				boolean isValid = false;
				Statement stmt = null;
				try {
					stmt = con.createStatement();
					isValid = stmt.execute(SQL);
				} catch (SQLException sqle) {
					isValid = false;
				} finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (Exception ex) {
						}
					}
				}
				if(!isValid) invalidTimes ++;
				return isValid;
			}else{
				start = false;
			}
		}
		return true;
	}
	public static long getEndtime() {
		return endtime;
	}
	public static int getInvalidTimes() {
		return invalidTimes;
	}
	public static int getValidateTimes() {
		return validateTimes;
	}
	public static boolean isStart() {
		return start;
	}
}
