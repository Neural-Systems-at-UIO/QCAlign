**Results**
===============

To export the results of either of the QC assessments go to: **File > Export stats**.

This generates a .TXT file that lists all the regions vertically, with four rows per region titled “accurate”, “inaccurate”, “uncertain” and “damaged” with marker counts for each. There is a row for the whole series and a row per section. 

.. list-table:: 
   :widths: 25 25 25
   :header-rows: 1

   * - Structure
     - Definition
     - Heading row 1, column 3
   * - Row 1, column 1
     - 
     - Row 1, column 3
   * - Row 2, column 1
     - Row 2, column 2
     - Row 2, column 3

For the QC assessment of the section images the counts may be used to calculate (%) damage per region for each section and for all the sections combined. The damage here represents tissue damage, poor quality labelling and imaging defects. 

For the QC assessment of the atlas-registration the counts may be used to calculate % “verified as accurate”, % “verified as inaccurate” and % that is uncertain for each region for each section and for all the sections combined. The calculations may be performed for smaller regions (a particular nuclei) or for broader regions subdivided into smaller regions (for example, the thalamus subdivided into thalamic nuclei).

**Post-processing the QUINT results**
----------------------------------------

The results from QCAlign can be used to guide decision-making regarding post-processing of the QUINT results. For example:

- a researcher may choose to exclude all regions that have more than 30% damage from the QUINT results.

- They may also choose to improve the registration of regions with > 5% inaccuracy. 

- If a lot of uncertainty is detected for a particular region (for example, the thalamus subdivided into thalamic nuclei), the results may prompt reporting at a coarser granularity level if higher confidence in the atlas-registration is required.  

