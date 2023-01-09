**What is QCAlign?**
====================

QCAlign was developed to support the use of the `QUINT workflow <https://quint-workflow.readthedocs.io/en/latest/#>`_ for high-throughput studies. It is a quality control tool that provides information about:

**1. The quality of the section images used as input to the QUINT workflow:** detect regions that are affected by tissue damage, labelling defects, artifacts, or errors in image acqusition (e.g. out-of-focus).

**2. The quality of the atlas-registration performed in the QUINT workflow:** detect regions that are poorly registered, or where the registration cannot be verified. 

3. In addition (not related to the quality control), QCAlign provides functionality for exploring the atlas hierarchy and deciding on a customized hierarchy level to use for the investigation.

**How does it work?**
---------------------

The quality control steps are based on systematic sampling, with a grid of points with fixed spacing positioned at random over the section images, with the option to superimpose the delineations from QuickNII or VisuAlign.

- For quality control of the section images there is a "damage" marker for marking up areas that are affected by tissue damage, artifacts, or errors in image aquisition. 

- For quality control of the atlas-registration there are "accurate", "inaccurate" and "uncertain" markers. The researcher working with the tool represents the “ground truth”, and uses visible landmarks in the sections and their anatomical expertise to assess the quality of the registration. 

**What can you do with the results?**
-----------------------------------

QCAlign generates two types of reports:

1. Reports with marker counts per region for the image series and per section image (.TXT format). This can be used to calculate percentage (%) damage per region based on the QC assessment of the section images; and % verified as accurate, % verified as inaccurate, and % that is uncertain for each region based on the QC assessment of the atlas-registration.

2. Hierarchy file in .TXT format. This represents the customized hierarchy level that is selected in QCAlign. This file is compatible with Nutil (version 0.8.0 and above) and can be used during QUINT analysis to generate customized reports. 


**More information on QUINT**
-----------------------------

The QUINT workflow supports spatial analysis of labelling in series of brain sections from mouse and rat based on registration to a reference atlas and feature extraction by segmentation. 

**QUINT tutorial**

.. raw:: html

   <iframe width="560" height="315" src="https://www.youtube.com/embed/n-gQigcGMJ0" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

.. note::
    **Visit our EBRAINS page for more information about the** `QUINT workflow; <https://ebrains.eu/service/quint/>`_ **and to find tutorials, examples of use, and software download information.**


