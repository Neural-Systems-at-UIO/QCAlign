**What is QCAlign?**
====================

The QuickNII - ilastik - Nutil (QUINT) workflow enables regional-quantification of labelled features in histological section images based on extraction of the labelled features by segmentation (with ilastik) and registration of the section images to a reference atlas (with QuickNII and VisuAlign). 

QCAlign was developed to support systematic post-processing of the QUINT results. As the quality of the QUINT results is dependent on the quality of the feature extraction, as well as the quality of the atlas-registration, QCAlign enables quality control of the section images (which has implications for the feature extraction), in addition to quality control of the atlas-registration. It also enables adjustment of the reference atlas hierarchy, which is a useful feature for exploring the hierarchy and for designing a customized level of the hierarchy for QUINT analysis. This feature was introduced as reference atlases contain boundaries that are not possible to discern in individual image series, which means that the hierarchy has to be adjusted for meaningful quality control of the atlas-registration.  

1. Quality control of the section images: detection of regions that are affected by tissue damage, labelling defects or errors in image acqusition (e.g. out-of-focus). 

2. Quality control of atlas-registration to the section images: detection of regions that are poorly registered or where the registration cannot be verified. This process involves adjusting the reference atlas hierarchy.

3. Exploring the reference atlas hierarchy: required for quality control of the atlas-registration, but also useful as a standalone procedure. 

The quality control steps are based on systematic sampling, with a grid of points with fixed spacing positioned at random over the section images, with the option to superimpose the delineations from QuickNII or VisuAlign.

- For quality control of the section images there is a "damage" marker for marking up areas that should be excluded from the analysis due to tissue damage, artifacts, or errors in image aquisition. 

- For quality control of the atlas-registration to the section images there is functionality for marking up whether a point is registered correctly, incorrectly, or of uncertain position based on visible landmarks within the section. In effect, the researcher working with the tool represents the “ground truth”, and uses the appearance of visible landmarks and their anatomical knowledge to assess the quality of the registration.

**QCAlign results**
-------------------

1. QCAlign generates reports in .txt format with marker counts per region for each section and for all the sections combined. 

- For the quality control of the section images this enables calculation of percentage (%) damage per region for each section and for all the sections combined. 
- For the quality control of the atlas-registration this enables calculation of % verified as accurate, % verified as inaccurate and % uncertainty per region, for each section and for all the sections combined.  

2. QCAlign generates customized atlas hierarchy files in .txt format that are compatible with the Nutil software used in the QUINT workflow.

**More information**
---------------------

Currently QCAlign supports the Allen Mouse Brain Atlas CCFv3, 2015 and 2017 and the Waxholm Space Atlas of the Spraque Dawley Rat v2 and v3. 

**PLEASE visit our EBRAINS page for more information about the QUINT workflow and to find tutorials, examples of use, demo videos and software download information.** 

https://ebrains.eu/service/quint/
