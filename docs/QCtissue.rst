**2. Quality control of the brain tissue and of the images**
=================================================================

To assess tissue integrity and quality of the histological slice images use the **(x)** marker: this is useful for identifying regions to exclude from the QUINT results due to tissue damage, artifacts, or poor quality imaging. This assessment is completed independently of the accurate, inaccurate, and uncertain series assessment.

How to perform the assessment
------------------------------

1. Start on the first slice of the series by clicking the \|<< arrow in the upper-right corner. Proceed to each subsequent slice by clicking >

2. In the slice viewer switch off the atlas overlay and mark up points that overlap the experimental slice. Points that fall outside of the experimental slice, or that do not overlay areas of damage, should be left blank. 
   
Select between four options by clicking the grid points with the mouse or keyboard: 

For this assessment, use the (X) marker only. Mark up all the grid points that are positioned over areas that should not be included in the analysis. For example, overlaying areas with tissue damage, artifacts such as air bubbles, or parts of the image that are out-of-focus.

+------------+--------------+-------------------+-------------------+
| **Marker** | **Mouse**    | **Keyboard**      | **Usage**         |
|            |              |                   |                   |
+============+==============+===================+===================+
| (X)        | Quadruple    | 5                 | Tissue damage,    |
|            | click        |                   | artifact or image |
|            |              |                   | out-of-focus      |
+------------+--------------+-------------------+-------------------+

4. Use the > in the far-right corner of the window to proceed through all slices.

5. Proceed until all the slices have been marked up. 

6. Adjust the atlas hierarchy to the level that you're interest in: this is typically the same level that you will use for Nutil analysis. If you'd like results for several hierarchy levels, it is possible to export the statistics several times. Simply adjust the hierarchy between exports (unlock the hierarchy in the "View" menu).  

5. Save the results in a JSON file. **File** > **Save as**.

6. Export statistics in txt format. **File** > **Export stats**.

The text file lists all the regions vertically, with
four rows per region titled “accurate”, “inaccurate”, “uncertain” and
“damaged” with the number of markers for each. There is a row for the whole series and a row per slice. 
      

