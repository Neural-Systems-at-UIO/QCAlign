**What is QCAlign?**
====================

The QuickNII - ilastik - Nutil (QUINT) workflow enables quantification of labelled features in section images from rodent brains based on extraction of the features by segmentation (with ilastik), and registration of the section images to a reference atlas (with QuickNII and VisuAlign). 

QCAlign was developed to support systematic post-processing of the QUINT results. As the quality of the QUINT results depends on the quality of the feature extraction, as well as the quality of the atlas-registration, QCAlign enables quality control of the section images (which has implications for the extraction), as well as quality control of the registration. As atlases contain boundaries that are not possible to discern in individual sections, QCAlign has functionality for adjusting the reference atlas hierarchy to a level that allows verification of the registration. This feature is also useful for exploring the hierarchy, and for designed a customized level to use for QUINT analysis. 

QCAlign enables:

1. Quality control of the section images: detection of regions that are affected by tissue damage, labelling defects, or errors in image acqusition (e.g. out-of-focus). 

2. Quality control of atlas-registration to the section images: detection of regions that are poorly registered or where the registration cannot be verified.

3. Exploring the reference atlas hierarchy: required for quality control of the atlas-registration, but also useful as a standalone feature. 

**How does it work?**
---------------------

The quality control steps are based on systematic sampling, with a grid of points with fixed spacing positioned at random over the section images, with the option to superimpose the delineations from QuickNII or VisuAlign.

- For quality control of the section images there is a "damage" marker for marking up areas that should be excluded from the analysis due to tissue damage, artifacts, or errors in image aquisition. 

- For quality control of the atlas-registration there are "accurate", "inaccurate" and "uncertain" markers for verifying the precision of the registration based on visible landmarks within the section. In effect, the researcher working with the tool represents the “ground truth”, and uses the appearance of visible landmarks and their anatomical knowledge to assess the quality of the registration.

**What do you do with the results?**
-----------------------------------

QCAlign generates two types of reports:

1. Reports in .txt format with marker counts per region for each section and for all the sections combined. 

- For the quality control of the section images this enables calculation of percentage (%) "damage" per region for each section and for all the sections combined. 
- For the quality control of the registration this enables calculation of % "verified as accurate", % "verified as inaccurate" and % that is uncertain for each region for each section and for all the sections combined.  

This information can be used to guide decision-making regarding post-processing of the QUINT results. For example, a researcher may choose to exclude all regions that have more than 30% damage from their QUINT results. 

2. QCAlign also generates hierarchy files in .txt format: these correspond to the customized level that is selected in the QCAlign tool. The .txt files are compatible with the Nutil software, and may be used to generate customized Nutil reports.

**More information on QUINT**
-----------------------------

Currently QCAlign supports the Allen Mouse Brain Atlas CCFv3, 2015 and 2017 and the Waxholm Space Atlas of the Spraque Dawley Rat v2 and v3. 

**PLEASE visit our EBRAINS page for more information about the QUINT workflow; and to find tutorials, examples of use, demo videos and software download information.** 

https://ebrains.eu/service/quint/
