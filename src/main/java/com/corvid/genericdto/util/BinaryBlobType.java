// /*
//  *  Copyright 2004 Blandware (http://www.blandware.com)
//  *
//  *  Licensed under the Apache License, Version 2.0 (the "License");
//  *  you may not use this file except in compliance with the License.
//  *  You may obtain a copy of the License at
//  *
//  *      http://www.apache.org/licenses/LICENSE-2.0
//  *
//  *  Unless required by applicable law or agreed to in writing, software
//  *  distributed under the License is distributed on an "AS IS" BASIS,
//  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  *  See the License for the specific language governing permissions and
//  *  limitations under the License.
//  */
// package com.corvid.genericdto.util;

// import org.hibernate.Hibernate;
// import org.hibernate.HibernateException;
// import org.hibernate.cfg.Environment;
// import org.hibernate.engine.spi.SessionImplementor;
// import org.hibernate.engine.spi.SharedSessionContractImplementor;
// import org.hibernate.type.StandardBasicTypes;
// import org.hibernate.type.Type;
// import org.hibernate.usertype.CompositeUserType;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.Serializable;
// import java.sql.Blob;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;

// /**
//  * <p>This is wrapper for both BinaryType and BlobType in order to give developer the ability to switch them via config</p>
//  * <p>Returned class is <code>byte[]</code>, that's why we should make conversion of BLOB</p>
//  * <p>User hibernate.binary_or_blob hibernate property in order to manage behaviour</p>
//  * <p/>
//  * <p><a href="BinaryBlobType.java.html"><i>View Source</i></a></p>
//  *
//  * @author Andrey Grebnev <a href="mailto:andrey.grebnev@blandware.com">&lt;andrey.grebnev@blandware.com&gt;</a>
//  * @version $Revision: 1.2 $ $Date: 2006/03/12 14:06:47 $
//  */
// public class BinaryBlobType implements CompositeUserType {

//     /**
//      * Default bufer size in order to copy InputStream to byte[]
//      */
//     protected static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

//     /**
//      * If it is </code>true</code> we use <code>BlobType</code>, otherwise <code>BinaryType</code>
//      */
//     protected boolean isBlob = false;

//     /**
//      * Creates new instance of BinaryBlobType
//      */
//     public BinaryBlobType() {
//         isBlob = "blob".equalsIgnoreCase(org.hibernate.internal.util.config.ConfigurationHelper.getString("hibernate.binary_or_blob", Environment.getProperties(), "binary"));
//     }


//     /**
//      * Get the "property names" that may be used in a
//      * query.
//      *
//      * @return an array of "property names"
//      */
//     public String[] getPropertyNames() {
//         return new String[]{"value"};
//     }

//     /**
//      * Get the corresponding "property types".
//      *
//      * @return an array of Hibernate types
//      */
//     public Type[] getPropertyTypes() {
//         if (isBlob)
//             return new Type[]{StandardBasicTypes.BLOB};
//         else
//             return new Type[]{StandardBasicTypes.BINARY};
//     }

//     /**
//      * Get the value of a property.
//      *
//      * @param component an instance of class mapped by this "type"
//      * @param property
//      * @return the property value
//      * @throws HibernateException
//      *
//      */
//     public Object getPropertyValue(Object component, int property) throws HibernateException {
//         return component;
//     }

//     /**
//      * Set the value of a property.
//      *
//      * @param component an instance of class mapped by this "type"
//      * @param property
//      * @param value     the value to set
//      * @throws HibernateException
//      *
//      */
//     public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
//       // 
//     }

//     /**
//      * The class returned by <tt>nullSafeGet()</tt>.
//      *
//      * @return Class
//      */
//     public Class<?> returnedClass() {
//         return StandardBasicTypes.BINARY.getReturnedClass();
//     }

//     /**
//      * Compare two instances of the class mapped by this type for persistence "equality".
//      * Equality of the persistent state.
//      *
//      * @param x
//      * @param y
//      * @return boolean
//      * @throws HibernateException
//      *
//      */
//     public boolean equals(Object x, Object y) throws HibernateException {
//         return StandardBasicTypes.BINARY.isEqual(x, y);
//     }

//     /**
//      * Get a hashcode for the instance, consistent with persistence "equality"
//      */
//     public int hashCode(Object x) throws HibernateException {
//         return StandardBasicTypes.BINARY.getHashCode(x, null);
//     }

//     /**
//      * Retrieve an instance of the mapped class from a JDBC resultset. Implementors
//      * should handle possibility of null values.
//      *
//      * @param rs      a JDBC result set
//      * @param names   the column names
//      * @param session
//      * @param owner   the containing entity
//      * @return Object
//      * @throws HibernateException
//      *
//      * @throws SQLException
//      */
//     public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
//         if (isBlob) {
//             Blob blob = (Blob) StandardBasicTypes.BLOB.nullSafeGet(rs, names, session, owner);
//             if (blob == null)
//                 return null;
//             else
//                 return copyData(blob.getBinaryStream());
//         } else {
//             return StandardBasicTypes.BINARY.nullSafeGet(rs, names, session, owner);
//         }
//     }

//     /**
//      * Write an instance of the mapped class to a prepared statement. Implementors
//      * should handle possibility of null values. A multi-column type should be written
//      * to parameters starting from <tt>index</tt>.
//      *
//      * @param st      a JDBC prepared statement
//      * @param value   the object to write
//      * @param index   statement parameter index
//      * @param session
//      * @throws HibernateException
//      *
//      * @throws SQLException
//      */
//     public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
//         if (isBlob) {
//             if (value == null)
//                 StandardBasicTypes.BLOB.nullSafeSet(st, value, index, session);
//             else {
//                 Blob blob = Hibernate.getLobCreator(session).createBlob((byte[]) value);
//                 StandardBasicTypes.BLOB.nullSafeSet(st, blob, index, session);
//             }
//         } else {
//             StandardBasicTypes.BINARY.nullSafeSet(st, value, index, session);
//         }
//     }

//     /**
//      * Return a deep copy of the persistent state, stopping at entities and at collections.
//      *
//      * @param value generally a collection element or entity field
//      * @return Object a copy
//      * @throws HibernateException
//      *
//      */
//     public Object deepCopy(Object value) throws HibernateException {
//         return StandardBasicTypes.BINARY.deepCopy(value, null);
//     }

//     /**
//      * Check if objects of this type mutable.
//      *
//      * @return boolean
//      */
//     public boolean isMutable() {
//         return StandardBasicTypes.BINARY.isMutable();
//     }

//     /**
//      * Transform the object into its cacheable representation. At the very least this
//      * method should perform a deep copy. That may not be enough for some implementations,
//      * however; for example, associations must be cached as identifier values. (optional
//      * operation)
//      *
//      * @param value   the object to be cached
//      * @param session
//      * @return a cachable representation of the object
//      * @throws HibernateException
//      *
//      */
//     public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {
//         return StandardBasicTypes.BINARY.disassemble(value, session, null);
//     }

//     /**
//      * Reconstruct an object from the cacheable representation. At the very least this
//      * method should perform a deep copy. (optional operation)
//      *
//      * @param cached  the object to be cached
//      * @param session
//      * @param owner   the owner of the cached object
//      * @return a reconstructed object from the cachable representation
//      * @throws HibernateException
//      *
//      */
//     public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
//         return StandardBasicTypes.BINARY.assemble(cached, session, owner);
//     }

//     /**
//      * During merge, replace the existing (target) value in the entity we are merging to
//      * with a new (original) value from the detached entity we are merging. For immutable
//      * objects, or null values, it is safe to simply return the first parameter. For
//      * mutable objects, it is safe to return a copy of the first parameter. However, since
//      * composite user types often define component values, it might make sense to recursively
//      * replace component values in the target object.
//      *
//      * @param original
//      * @param target
//      * @param session
//      * @param owner
//      * @return first parameter
//      * @throws HibernateException
//      *
//      */
//     public Object replace(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {
//         return StandardBasicTypes.BINARY.replace(original, target, session, owner, null);
//     }


//     /**
//      * Copy data from InputStream into byte[]
//      *
//      * @param input source
//      * @return the resulted array
//      */
//     protected byte[] copyData(InputStream input) {
//         ByteArrayOutputStream output = null;
//         try {
//             output = new ByteArrayOutputStream();
//             byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
//             int n = 0;
//             while (-1 != (n = input.read(buffer))) {
//                 output.write(buffer, 0, n);
//             }
//             return output.toByteArray();

//         } catch (IOException ex) {
//             throw new RuntimeException("Cannot copy data from InputStream into byte[]", ex);
//         } finally {
//             try {
//                 input.close();
//             } catch (IOException ex2) {
//                 //do nothing
//             }
//             try {
//                 output.close();
//             } catch (IOException ex2) {
//                 //do nothing
//             }
//         }
//     }


//     @Override
//     public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
//             throws HibernateException, SQLException {
//         return null;
//     }


//     @Override
//     public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
//             throws HibernateException, SQLException {
        
//     }


//     @Override
//     public Serializable disassemble(Object value, SharedSessionContractImplementor session) throws HibernateException {
//         return null;
//     }


//     @Override
//     public Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner)
//             throws HibernateException {
//         return null;
//     }


//     @Override
//     public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner)
//             throws HibernateException {
//         return null;
//     }
// }