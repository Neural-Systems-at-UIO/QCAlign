**What is QCAlign?**
====================

The QuickNII - ilastik - Nutil (QUINT) workflow enables quantification of labelled features in section images from rodent brains based on extraction of the features by segmentation (with ilastik), and registration of the section images to a reference atlas (with QuickNII and VisuAlign). 

QCAlign was developed to support systematic post-processing of the QUINT results. As the quality of the QUINT results depends on the quality of the feature extraction, as well as the quality of the atlas-registration, QCAlign enables quality control of the section images (which has implications for the extraction), as well as quality control of the registration. As reference atlases contain boundaries that are not possible to discern in individual sections, QCAlign has functionality for adjusting the atlas hierarchy to a level that allows verification of the registration. This feature is also useful for exploring the hierarchy, and for designed a customized level to use for QUINT analysis. 

**1. Quality control assessment of the section images:** detection of regions that are affected by tissue damage, labelling defects, or errors in image acqusition (e.g. out-of-focus). 

**2. Quality control assessment of atlas-registration to the section images:** detection of regions that are poorly registered or where the registration cannot be verified.

**3. Exploring the reference atlas hierarchy:** required for quality control of the atlas-registration, but also useful as a standalone feature. 

**How does it work?**
---------------------

The quality control steps are based on systematic sampling, with a grid of points with fixed spacing positioned at random over the section images, with the option to superimpose the delineations from QuickNII or VisuAlign.

- For quality control of the section images there is a "damage" marker for marking up areas that should be excluded from the analysis due to tissue damage, artifacts, or errors in image aquisition. 

- For quality control of the atlas-registration there are "accurate", "inaccurate" and "uncertain" markers for verifying the registration based on visible landmarks within the section. In effect, the researcher working with the tool represents the “ground truth”, and uses the appearance of visible landmarks and their anatomical knowledge to assess the quality of the registration.

**What can you do with the results?**
-----------------------------------

QCAlign generates two types of reports:

1. Reports with marker counts per region for each section and for all the sections combined (.TXT format). This can be used to calculate percentage (%) damage per region based on the QC assessment of the section images; and % verified as accurate, % verified as inaccurate, and % that is uncertain for each region based on the QC assessment of the atlas-registration.

2. Hierarchy files in .TXT format. These are compatible with the customized report feature in Nutil and correspond to the customized hierarchy level that is selected in QCAlign. It may therefore be used to perform customized analysis for this hierarchy level. 


**More information on QUINT**
-----------------------------

Currently QCAlign supports the Allen Mouse Brain Atlas CCFv3, 2015 and 2017 and the Waxholm Space Atlas of the Spraque Dawley Rat v2 and v3. 

**PLEASE visit our `EBRAINS page <https://ebrains.eu/service/quint/>`_ for more information about the QUINT workflow; and to find tutorials, examples of use, demo videos and software download information.


