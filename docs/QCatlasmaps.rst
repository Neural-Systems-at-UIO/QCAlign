.. include:: .special.rst

**1. Quality control of registration alignment**
=========================================================

1. Start on the first slice of the series by clicking the \|<< arrow in the
   upper-right corner. Proceed to each subsequent slice by clicking >

2. In the slice viewer, only mark points that overlap the
   experimental slice. Points that fall outside of the experimental slice should be left blank. 
   
   Select between four options by clicking the grid points with the mouse or keyboard. For quality control assessment of
   the atlas maps, use the **(+), (-)** and **(?)** markers only.

   -  :green:'**Accurate: (+)' Single click, Keyboard shortcut: 2** Use this
      marker for grid points that are *correctly* located in the
      assigned region: this is verified by the anatomist based on
      landmarks. The region boundaries are distinct enough to make this
      call.

   -  **Inaccurate: (-) Double click, Keyboard shortcut: 3** Use this
      marker for grid points that are *incorrectly* located in the
      assigned region: this is verified by the anatomist based on
      landmarks. The region boundaries are distinct enough to make this
      call.

   -  **Uncertain: (?) Triple click, Keyboard shortcut: 4** Use this
      marker if you are uncertain whether the grid point is located in
      the assigned region or not: the region boundaries are not distinct
      enough to make this call. This does not mean that the registration
      is incorrect, only that verification is not possible based on the
      available imaging data alone.
      
**IMPORTANT**
The user should ONLY mark up (+) / (-) when certain of the precision of the atlas registration based on visible landmarks within the section. The (?) should be used in all instances when the user is unsure of the correct assignment due to undiscernible boundaries or limited anatomical knowledge.
A useful anatomy resource when working with QCAlign is the Allen Brain Atlas Interactive Brain Viewer (coronal atlas, Nissl stain). This shows the Allen Mouse Brain Atlas applied to Nissl stained sections and can be used as a guide.
Example images for each marker are given at the end of this section. 

**RECOMMENDATION**
For the purpose of the assessment, it is best to switch the atlas overlay “off” so that only the brain tissue is visible. By hovering over any point on the brain tissue, a text overlay is displayed that reveals the atlas region to which the point is registered. Use this information, any visible landmarks, and your anatomical knowledge to assess whether the point is registered correctly, incorrectly, or of uncertain position. 
It is possible to toggle the atlas overlay “on” and off” between each point assessment to get an overview of the atlas delineations. While it can be helpful with an overview, try not to let the delineations affect your decision-making as this will bias the outcome. If in doubt, always mark up (?).


3. To get an overview of the grid points and easily identify grid points
   that remain unmarked within a slice, go to **View** > **Overview,**
   or press the spacebar.

4. Use the > in the far-right corner of the window to proceed through
   all slices and add markers to all grid points in the series.

5. Save results in a JSON file. **File** > **Save as**.

6. Export statistics in txt file. **File** > **Export stats**.

This generates a text file with all the regions listed vertically, with
four rows per region titled “accurate”, “inaccurate”, “uncertain” and
“damaged” that list the number of markers for each.

Autofill grid markers
---------------------

To reduce the workload for the atlas map assessment, it is possible to
autofill all the markers as **(+)**, with the exception of the markers
that are closest to the region boundaries. To enable this feature, go to
**Series Prefill grid, except damage markers.** The markers closest to
the region boundaries remain unmarked. The user must manually mark up
the points that fall along the region boundaries to ensure precise
marker classification. Grid points along regional borders are more prone
to registration errors.

-  This feature was introduced to increase efficiency, whilst minimizing
   bias introduced by autofill.
