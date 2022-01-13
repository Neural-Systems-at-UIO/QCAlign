**Adjusting the hierarchy**
============================

An adjustable hierarchy panel is located on the left-hand side of the
QCAlign viewer window. To adjust the regions included on the slice atlas
viewer, click the ▼ arrow next to each region to reveal subdivisions or
► arrow to collapse the region into its parent structure.

The default setting reveals the finest granularity level of the atlas,
with all regions displayed (as shown in the example). From this
position, gradually collapse the hierarchy until a customized level is
achieved. The appearance of the atlas overlay in the viewer window
automatically adjusts to match the customized hierarchy selected.

Once you have created a customized hierarchy level that you would like
to work with, save the QCAlign JSON file by clicking **File > Save as**.
To reduce the need to adjust the hierarchy with each use, saved
customized JSON hierarchy sheets may be imported and implemented when
assessing multiple brain series.

**Example: All regions of the Allen Brain Atlas are expanded and visible
in the viewer window**

.. image:: vertopal_cbedec83746b4aa08b3d6abec4c06604/media/image2.png
   :width: 5.04087in
   :height: 3.20833in

**Hierarchy import**
-----------------

1. To import a customized hierarchy level saved as a JSON file, go to
   **File** > **Import Hierarchy**.

2. Navigate to the JSON file and import. This only imports the
   customized hierarchy: not the markers, point spacing or other
   settings from the QCAlign JSON file.

**Hierarchy export for use with Nutil Quantifier**
-----------------------------------------------

1. To export a customized hierarchy level to be used in Nutil, go to
   **File** > **export hierarchy sheet.**

2. When later navigating in Nutil, this .JSON file can be imported as a
   custom report file to receive quantification output for those
   specified regions.

.. _section-1:
