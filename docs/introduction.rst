**What is QCAlign?**
====================

QCAlign was developed for validation of QUINT performance and to guide decision-making regarding post-processing of the QUINT results. It enables:

1.	Exploration of the reference atlas hierarchy.

2.	Quality control of the atlas maps generated with QuickNII and VisuAlign.

3.	Quality control of the histological slice images.

The quality control steps are based on systematic sampling, with a grid of points with a fixed spacing positioned at random over the section images, with the option to superimpose the atlas maps. 

For the quality control of the atlas maps there is functionality for marking up whether a point is registered correctly, incorrectly, or of uncertain position based on visible landmarks within the section. In effect, the user working with the tool represents the “ground truth”, and uses the appearance of visible landmarks and their anatomical knowledge to validate the quality of the registration. 

For quality control of the histological slice images there is a damage marker for marking up areas that should be excluded from the analysis due to tissue damage, artifacts, or errors in image aquisition. As output, QCAlign generates reports with marker counts per region. This enables calculation of percentage (%) accuracy, % inaccuracy and % uncertainty for the atlas registration per region, for the whole brain and per slice; and % damage per region based on the quality control of the slice images. 

As the quality control of the atlas maps and of the slice images use similar functionality, but are performed separately based on different protocols, this user manual starts with an overview of QCAlign functionality, and then provides step-by-step instructions for how to perform each type of assessment. 
As an additional feature, QCAlign may be used to explore the reference atlas hierarchy. This can be done as part of quality control, or as an independent procedure. QCAlign enables export of customized atlas hierarchy files that are compatible with the Nutil Quantifier software used in the QUINT workflow. 

Currently QCAlign supports the Allen Mouse Brain Atlas CCFv3, 2015 and 2017 and the Waxholm Space Atlas of the Spraque Dawley Rat v2 and v3. 

**PLEASE visit our EBRAINS page for more information about the QUINT workflow and to find tutorials, examples of use, demo videos and software download information.** 

https://ebrains.eu/service/quint/
