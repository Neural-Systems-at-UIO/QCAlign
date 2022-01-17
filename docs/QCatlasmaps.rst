.. include:: .special.rst

**1. Quality control of registration alignment**
=========================================================

1. Start on the first slice of the series by clicking the \|<< arrow in the
   upper-right corner. Proceed to each subsequent slice by clicking >

2. In the slice viewer, only mark atlas points that overlap the
   experimental slice. Select between four options by clicking the grid
   points with the mouse or keyboard. For quality control assessment of
   the atlas maps, use the **(+), (-)** and **(?)** markers only.

   -  **Accurate: :green: `(+)` Single click, Keyboard shortcut: 2** Use this
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
