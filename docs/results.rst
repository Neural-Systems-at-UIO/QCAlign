**Results**
===============

To export the results of either of the QC assessments go to: **File > Export stats**.

This generates a .TXT file that lists all the regions vertically. For each region there are five rows titled **N/A**, **Accurate**, **Inaccurate**, **Uncertain**, and **Damaged** with corresponding marker counts. There is a row for the whole series and a row per section. 


.. figure:: vertopal_cbedec83746b4aa08b3d6abec4c06604/media/Results.PNG
   :class: with-border 

.. list-table:: 
   :widths: 50 50
   :header-rows: 1

   * - Structure
     - Definition
   * - Total grid
     - Total no. of markers in the whole grid
   * - Total atlas
     - Total no. of markers within the atlas map
   * - Clear Label
     - All markers that fall outside of the atlas maps
     
.. note::
   Total no. of markers within the atlas maps = Sum of all **Total** markers - Sum of all **Clear label** markers
     
For the QC assessment of the section images the counts may be used to calculate (%) damage per region for each section and for all the sections combined. The damage here represents tissue damage, poor quality labelling and imaging defects. 

For the QC assessment of the atlas-registration the counts may be used to calculate % “verified as accurate”, % “verified as inaccurate” and % that is uncertain for each region for each section and for all the sections combined. The calculations may be performed for smaller regions (a particular nuclei) or for broader regions subdivided into smaller regions (for example, the thalamus subdivided into thalamic nuclei).

**Post-processing the QUINT results**
----------------------------------------

The results from QCAlign can be used to guide decision-making regarding post-processing of the QUINT results. For example:

- a researcher may choose to exclude all regions that have more than 30% damage from the QUINT results.

- They may also choose to improve the registration of regions with > 5% inaccuracy. 

- If a lot of uncertainty is detected for a particular region (for example, the thalamus subdivided into thalamic nuclei), the results may prompt reporting at a coarser granularity level if higher confidence in the atlas-registration is required.  

