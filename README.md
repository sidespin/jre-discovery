[![](https://img.shields.io/eclipse-marketplace/v/jre-discovery?color=light-green)](https://marketplace.eclipse.org/content/jre-discovery)
# JRE Discovery

This feature allows the automatic discovery of managed Java Runtime Environments in Eclipse. 
It currently detects JREs managed by :
- [SDKMan](https://sdkman.io/), 
- [asdf-java](https://github.com/halcyon/asdf-java), 
- [Jabba](https://github.com/shyiko/jabba) 
- [JBang](https://www.jbang.dev/)

Managed JREs will be automatically discovered on Eclipse startup, or, while running, when added by their respective Java managers.

![Detected JREs](images/jre-discovery.png)

Automatic detection can be disabled from the `JRE Discovery` preference page:

<img src="images/jre-discovery-prefs.png" width="600" />

Installation
------------
_JRE Discovery_ is available in the [Eclipse Marketplace](https://marketplace.eclipse.org/content/jre-discovery). Drag to the following button to your running Eclipse workspace. (⚠ *Requires the Eclipse Marketplace Client*)

[![Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace the Client](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.svg)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=5514555 "Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client")

Alternatively, in Eclipse:

- open Help > Install New Software...
- work with: `https://github.com/sidespin/jre-discovery/releases/download/latest/`
- expand the category and select the JRE Discovery Feature
- proceed with the installation
- restart Eclipse


Build
-----

Open a terminal and execute the following command:
- On Linux/Mac:

> ./mvnw clean verify
    
- On Windows:
    
> mvnw.cmd clean verify

You can then install the generated update site from `io.sidespin.jre.discovery.site/target/repository`

# License

Licensed under the [EPL-2.0](https://www.eclipse.org/legal/epl-2.0/).