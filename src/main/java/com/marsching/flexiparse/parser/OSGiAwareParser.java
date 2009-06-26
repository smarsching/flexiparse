/* 
 * fleXiParse - Copyright 2009 Sebastian Marsching
 * 
 * This file is part of fleXiParse.
 * 
 * fleXiParse is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * fleXiParse is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with fleXiParse.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.marsching.flexiparse.parser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.xml.sax.InputSource;

import com.marsching.flexiparse.parser.exception.ParserConfigurationException;
import com.marsching.flexiparse.parser.exception.ParserException;

/**
 * Parser implementation that looks for configuration files in OSGi bundles
 * and uses the class loader of the file's bundle to load classes.
 * 
 * @author Sebastian Marsching
 */
public class OSGiAwareParser extends XMLConfiguredParser {
	
    
    private class BundleDelegatingClassLoader extends ClassLoader {
        
        private Bundle bundle;
        
        public BundleDelegatingClassLoader(Bundle bundle) {
            this.bundle = bundle;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                return bundle.loadClass(name);
            } catch (ClassNotFoundException e) {
                throw new ClassNotFoundException("Could not load class " + name + " from bundle " + bundle.getSymbolicName() + "[" + bundle.getBundleId() + "]", e);
            } catch (NoClassDefFoundError e) {
                NoClassDefFoundError e1 = new NoClassDefFoundError("Could not load class " + name + " from bundle " + bundle.getSymbolicName() + "[" + bundle.getBundleId() + "]");
                e1.initCause(e);
                throw e1;
            }
        }

        @Override
        protected URL findResource(String name) {
            return bundle.getResource(name);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Enumeration<URL> findResources(String name) throws IOException {
            return bundle.getResources(name);
        }

    }

	/**
	 * Default constructor, looks for configuration at
	 * "META-INF/com/marsching/flexiparse/configuration/configuration.xml".
	 * 
	 * @param bundleContext bundle context that is used to search for bundles
	 * @throws ParserException
	 */
	public OSGiAwareParser(BundleContext bundleContext) throws ParserException {
		this(bundleContext, "com/marsching/flexiparse/configuration/configuration.xml");
	}
	
	/**
	 * Creates a parser instance using the specified configuration
	 * location.
	 * 
	 * @param bundleContext bundle context that is used to search for bundles
	 * @param configurationLocation location where to look for 
	 * 	 configuration files
	 * @throws ParserException if an error occurs while loading
	 *   the configuration files
	 */
	public OSGiAwareParser(BundleContext bundleContext, String configurationLocation) throws ParserException {
		super();
		
		int lastSlashPos = configurationLocation.lastIndexOf('/');
		String path;
		String filename;
		if (lastSlashPos == -1) {
		    path = "/";
		    filename = configurationLocation;
		} else {
		    path = configurationLocation.substring(0, lastSlashPos);
		    filename = configurationLocation.substring(lastSlashPos + 1);
		}
		Bundle[] bundles = bundleContext.getBundles();
		for (int i = 0; i < bundles.length; i++) {
            Bundle bundle = bundles[i];
            if (bundle.getState() < Bundle.INSTALLED) {
                continue;
            }
            if (bundle.getHeaders().get(Constants.FRAGMENT_HOST) != null) {
                // Ignore fragment bundles
                continue;
            }
            @SuppressWarnings("unchecked")
            Enumeration<URL> en = bundle.findEntries(path, filename, false);
            while (en != null && en.hasMoreElements()) {
                URL url = en.nextElement();
                String systemId;
                try {
                    systemId = url.toURI().toASCIIString();
                } catch (URISyntaxException e) {
                    // Use url.toExternalForm() instead
                    systemId = url.toExternalForm();
                }
                InputSource source;
                try {
                    source = new InputSource(url.openStream());
                } catch (IOException e) {
                    throw new ParserConfigurationException("Could not load configuration from " + url.toString(), e);
                }
                if (systemId != null) {
                    source.setSystemId(systemId);
                }
                addConfigurationSource(source, new BundleDelegatingClassLoader(bundle));
            }
        }
	}
}
