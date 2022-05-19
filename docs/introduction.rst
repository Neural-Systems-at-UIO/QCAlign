**What is QCAlign?**
====================

QCAlign was developed to support the use of the QuickNII - ilastik - Nutil workflow (QUINT) for high-throughput studies. 



QUINT enables quantification of labelled features in section images from rodent brains based on extraction of the features by segmentation and registration of the section images to a reference atlas. As the quality of the QUINT results depends on the quality of the feature extraction, as well as the quality of the atlas-registration, QCAlign enables quality control of the section images (which has implications for the extraction), as well as quality control of the registration. 

As reference atlases contain boundaries that are not possible to discern in individual sections, QCAlign has functionality for adjusting the atlas hierarchy to a level that allows verification of the registration. This feature is also useful for exploring the hierarchy, and for designing a customized level to use for QUINT analysis. 

**QUINT tutorial**

.. raw:: html

   <iframe width="560" height="315" src="https://www.youtube.com/embed/n-gQigcGMJ0" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

**QCAlign functionality**
------------------------

QCAlign is a quality control tool that provides information about:

**1. The quality of the section images used as input to the QUINT workflow:** detect regions that are affected by tissue damage, labelling defects, artifacts, or errors in image acqusition (e.g. out-of-focus). 

**2. The quality of the atlas-registration performed in the QUINT workflow:** detect regions that are poorly registered, or where the registration cannot be verified.

In addition (not related to the quality control), QCAlign makes it easier for the user to explore the atlas hierarchy and decide on a customized hierarchy level for the investigation.

**How does it work?**
---------------------

The quality control steps are based on systematic sampling, with a grid of points with fixed spacing positioned at random over the section images, with the option to superimpose the delineations from QuickNII or VisuAlign.

- For quality control of the section images there is a "damage" marker for marking up areas that are affected by tissue damage, artifacts, or errors in image aquisition. 

- For quality control of the atlas-registration there are "accurate", "inaccurate" and "uncertain" markers. The researcher working with the tool represents the “ground truth”, and uses the appearance of visible landmarks in the sections and their anatomical expertise to assess the quality of the registration. 

**What can you do with the results?**
-----------------------------------

QCAlign generates two types of reports:

1. Reports with marker counts per region for the whole series and per section (.TXT format). This can be used to calculate percentage (%) damage per region based on the QC assessment of the section images; and % verified as accurate, % verified as inaccurate, and % that is uncertain for each region based on the QC assessment of the atlas-registration.

2. Hierarchy file in .TXT format. This represents the customized hierarchy level that is selected in QCAlign. They are compatible with Nutil and can be imported during QUINT analysis to generate customized reports. 


**More information on QUINT**
-----------------------------

Currently QCAlign supports the Allen Mouse Brain Atlas CCFv3, 2015 and 2017 and the Waxholm Space Atlas of the Spraque Dawley Rat v2, v3 and v4. 

.. note::
    **PLEASE visit our EBRAINS page for more information about the** `QUINT workflow; <https://ebrains.eu/service/quint/>`_ **and to find tutorials, examples of use, and software download information.**


