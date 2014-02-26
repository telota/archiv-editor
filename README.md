Archiv-editor
=============
The archive-editor is an application-program; it supports the scientific work with biographical data. With a central repository several scientist can work on the same data. A local database holds the information until it will be synchronized with the central repository. 

more information see: http://pdr.bbaw.de/ae
English: http://pdr.bbaw.de/english

Installation
------------
1. Install Eclipse RCP/RAP distribution.

2. Clone git, that is import all projects from archiv-editor.git

3. Go to plugin org.bbaw.pdr.ae.standalone, open Product-Configuration file org.bbaw.pdr.ae.featureProduct.product in Product-Editor (PDE-Tools)

4. Overview-Page, click on "Launch an Eclipse application". This will create a Run-Configuration based on the Product-Configuration. Running this Run-Configuration will fail at the first attempt. Then go to Menu>>Run>>Run Configurations... Open the newly created Run-Configuration, select Tab Plug-ins, click "Add Required Plug-ins", then hit Run. OR:  Create run configuration and add required plugins and features.

5. Archiv-Editor will automatically create 2 folders in the parent folder of your workspace, one AEConfig with a default configuration file, and baseXHOME with database files for the local baseX-database.

6. You will see the Installation-Dialog, change settings if needed, click "Save".

7. Next you will see the Repository-Connection-Dialog. Click "Cancel", you don't need to enter a valid repository connection. For more, see the manual at pdr.bbaw.de/ae

8. Now you'll be requested to enter a username: enter eg. admin/admin, or user/user

9. Eventually, the main perspective will show up.


Build
-----

Use PDE-Product Export from Product Configuration Editor, Overview tab, Export Product.