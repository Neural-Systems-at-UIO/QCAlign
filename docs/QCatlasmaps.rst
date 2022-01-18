**1. Quality control of registration alignment**
=========================================================

How to perform the assessment
------------------------------

1. Start on the first slice of the series by clicking the \|<< arrow in the upper-right corner. Proceed to each subsequent slice by clicking >

2. In the slice viewer, mark points that overlap the experimental slice. Points that fall outside of the experimental slice should be left blank. 
   
   Select between four options by clicking the grid points with the mouse or keyboard. For quality control assessment of
   the atlas maps, use the **(+), (-)** and **(?)** markers only.
   
+-------------+---------------+-------------------+---------------------------------------------------------------------------------------------------------+   
|  **Marker** |   **Mouse**   |    **Keyboard**   |                    **Usage**                                                                            |
|             |               |    **shortcut**   |                                                                                                         |
+=============+===============+===================+=========================================================================================================+
|  **(+)**    |  Single click |         2         |  Use this marker for grid points that are correctly located in the assigned region: this is verified by |      
|             |               |                   |  the anatomist based on landmarks. The regions boundaries are distinct enough to make this call.        |       
+-------------+---------------+-------------------+---------------------------------------------------------------------------------------------------------+
|  **(-)**    |  Double click |         3         |  Use this marker for grid points that are incorrectly located in the assigned region: this is verified  |
|             |               |                   |  by the anatomist based on landmarks. The region boundaries are distinct enough to make this call.      |
+-------------+---------------+-------------------+---------------------------------------------------------------------------------------------------------+
|  **(?)**    |  Triple click |         4         |  Use this marker if you are uncertain whether a grid point is located in the assigned region: the region|
|             |               |                   |  boundaries are not distinct enough to make this call. This does not mean that the registration is      |        |             |               |                   |  incorrect: only that the verification is not possible based on the available imaging data.             |
+-------------+---------------+-------------------+---------------------------------------------------------------------------------------------------------+
      
3. To get an overview of the grid points, and to easily identify grid points that remain unmarked within a slice, go to **View** > **Overview,** or press the spacebar.

4. Use the > in the far-right corner of the window to proceed through all slices.

5. Save the results in a JSON file. **File** > **Save as**.

6. Export statistics in txt file. **File** > **Export stats**.

The text file lists all the regions vertically, with
four rows per region titled “accurate”, “inaccurate”, “uncertain” and
“damaged” with the number of markers for each. There is a row for the whole series and a row per slice. 
      
Best practise 
---------------

**IMPORTANT**

As QCAlign is used for validation, the user’s input represents the anatomical “ground truth” for assessing the quality of the reference atlas registration to the sections. 

- The user should ONLY mark up (+) / (-) when certain of the precision of the atlas registration based on visible landmarks within the section. The (?) should be used in all instances when the user is unsure of the correct assignment due to undiscernible boundaries or limited anatomical knowledge.
- For the purpose of the assessment, it is best to switch the atlas overlay “off” so that only the brain tissue is visible. 
- By hovering over any point on the brain tissue, a text overlay is displayed that reveals the atlas region to which the point is registered. Use this information, any visible landmarks, and your anatomical knowledge to assess whether the point is registered correctly, incorrectly, or of uncertain position. 
- It is possible to toggle the atlas overlay “on” and off” between each point assessment to get an overview of the atlas delineations. While it can be helpful with an overview, try not to let the delineations affect your decision-making as this will bias the outcome. If in doubt, always mark up (?).
- A useful anatomy resource when working with QCAlign is the Allen Brain Atlas Interactive Brain Viewer (coronal atlas, Nissl stain). This shows the Allen Mouse Brain Atlas applied to Nissl stained sections and can be used as a guide.

Examples of Accurate, Inaccurate, and Uncertain Markings
--------------------------------------------------------

**Section with clearly visible landmarks
demonstrating use of (+) and (-) markers.**

|image1|\ 

There are clear boundaries between all the structures in this slice due
to differences in tissue appearance, and clearly visible cell layers in
the case of the hippocampus. All regions can be marked up with
certainty: (+) or (-). One of the points is registered to the ventricle
despite being located in the fiber tract and has been marked up with
**(-)**. All the other markers are marked up with **(+).**

**Section with clearly visible landmarks and inaccurate
registration.**

.. image:: vertopal_cbedec83746b4aa08b3d6abec4c06604/media/image9.jpeg
   :alt: Z:\NESYS_Lab\PhD_project_Yates_Sharon\Jackson_article\QControl\User_manual\Inaccurate.jpg
   :width: 4.94697in
   :height: 2.42422in

**ESection with ambiguous boundaries demonstrating use of the
(?) marker.**

.. image:: vertopal_cbedec83746b4aa08b3d6abec4c06604/media/image10.jpeg
   :width: 6.3in
   :height: 4.82222in

There are clear boundaries between the lateral ventricle, the
caudoputamen, the corpus callosum, and the isocortex in this slice.
These regions can be marked up with certainty (+) and (-).

There is less certainty for the outer boundary of the cortical subplate,
and the boundary between the isocortex, entorhinal area and olfactory
area. These areas have been marked up with transparent red circles and
have been assigned the **(?)** marker.

The placement of the **(?)** will vary for regions from slice to slice
depending on the distinction of visual landmarks.

.. |image1| image:: vertopal_cbedec83746b4aa08b3d6abec4c06604/media/image8.jpeg
   :width: 5.85417in
   :height: 4.77083in
.. |image2| image:: vertopal_cbedec83746b4aa08b3d6abec4c06604/media/image11.jpeg
   :width: 3.84306in
   :height: 4.51181in

