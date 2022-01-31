**What is QCAlign?**
====================

The QuickNII - ilastik - Nutil (QUINT) workflow enables regional-quantification of labelled features in histological section images based on extraction of the labelled features by segmentation (with ilastik) and registration of the section images to a reference atlas (with QuickNII and VisuAlign). 

As the quality of the QUINT results is dependent on the quality of the feature extraction, as well as the quality of the atlas-registration, QCAlign enables quality control of the section images (which has implications for the segmentation quality), in addition to quality control of the atlas-registration. Furthermore, ses contain boundaries that are not possible to discern in individual datasets, the reference atlas hierarchy has to be adjusted for all image series to enable quality control of the atlas-registration. QCAlign therefore supports adjustment of the reference atlas hierarchy, which is done as part of the quality control of the registration, but is also a useful feature in itself as it enables hierarchy exploration. QCAlign thereby supports systematic post-processing of the QUINT results by detecting regions that are affected by tissue damage, labelling defects, or errors in image acquistion; or where the registration accuracy is either low or uncertain.

It enables:

1. Quality control of the section images: detection of regions that are affected by tissue damage, labelling defects or errors in image acqusition (e.g. out-of-focus). 

2. Quality control of atlas-registration to the section images: detection of regions that are poorly registered or where the registration cannot be verified. This process involves adjusting the reference atlas hierarchy.

3. Exploring the reference atlas hierarchy. 

The quality control steps are based on systematic sampling, with a grid of points with fixed spacing positioned at random over the section images, with the option to superimpose the delineations from QuickNII or VisuAlign.

- For quality control of the section images there is a "damage" marker for marking up areas that should be excluded from the analysis due to tissue damage, artifacts, or errors in image aquisition. 

- For quality control of the atlas-registration to the section images there is functionality for marking up whether a point is registered correctly, incorrectly, or of uncertain position based on visible landmarks within the section. In effect, the researcher working with the tool represents the “ground truth”, and uses the appearance of visible landmarks and their anatomical knowledge to assess the quality of the registration. 

- As output, QCAlign generates reports in .txt format with marker counts per region for each section and for all the sections combined. 
For the quality control of the section images this enables calculation of percentage (%) damage per region for each section and for all the sections combined.
For the quality control of the atlas-registration this enables calculation of % verified as accurate, % verified as inaccurate and % uncertainty per region, for each section and for all the sections combined. 

QCAlign may also used to explore the reference atlas hierarchy. This can be done as part of quality control, or as an independent procedure.  

QCAlign enables export of customized atlas hierarchy files that are compatible with the Nutil software used in the QUINT workflow. 


Currently QCAlign supports the Allen Mouse Brain Atlas CCFv3, 2015 and 2017 and the Waxholm Space Atlas of the Spraque Dawley Rat v2 and v3. 

**PLEASE visit our EBRAINS page for more information about the QUINT workflow and to find tutorials, examples of use, demo videos and software download information.** 

https://ebrains.eu/service/quint/
