**1. QC Assessment of the Section Images**
============================================

The preparation of section images is technically difficult, involving sectioning, labelling, mounting and imaging. Even with the utmost care taken during preparation, brain sections may be deformed or torn, poorly labelled or covered by artifacts, or affected by imaging defects such as parts of the image being out-of-focus. Defects such as these have implications for the quality of the segmentations that can be achieved. This assessment detects regions that are affected, and enables calculation of % damage per region.

The assessment is performed independently of the atlas-registration assessment with the atlas delineations switched off.   

How to perform the assessment
------------------------------

1. Start on the first section of the series by clicking the \|<< arrow in the upper-right corner. Proceed to each subsequent section by clicking >

2. In the section viewer switch off the atlas overlay and mark up points that overlap areas of damage on the experimental section. Points that fall outside of the experimental section, or that do not overlay areas of damage, should be left blank. 

For this assessment, use the **(x)** marker only. Mark up all the grid points that are positioned over areas that should not be included in the analysis: for example, as they are overlaying areas of tissue damage, artifacts such as air bubbles, or parts of the image that are out-of-focus.

+------------+--------------+-------------------+-------------------+
| **Marker** | **Mouse**    | **Keyboard**      | **Usage**         |
|            |              |                   |                   |
+============+==============+===================+===================+
| (x)        | Quadruple    | 5                 | Tissue damage,    |
|            | click        |                   | artifact or image |
|            |              |                   | out-of-focus      |
+------------+--------------+-------------------+-------------------+

4. Use the > in the far-right corner of the window to proceed through all sections.

5. Proceed until all the sections have been marked up. 

6. Adjust the atlas hierarchy to the level that you're interest in: this is typically the same level that you will use for Nutil analysis. If you'd like results for several hierarchy levels it's possible to export the statistics several times. Simply adjust the hierarchy between exports (unlock the hierarchy in the "View" menu).  

5. Save the results in a JSON file. **File** > **Save as**.

6. Export statistics in txt format. **File** > **Export stats**.

The text file lists all the regions vertically, with
four rows per region titled “accurate”, “inaccurate”, “uncertain” and
“damaged” with the number of markers for each. There is a row for the whole series and a row per slice. 
      

