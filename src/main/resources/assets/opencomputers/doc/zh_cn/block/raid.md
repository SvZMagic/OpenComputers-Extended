# Raid

![40 man instance.](oredict:oc:raid)

Raid 磁盘阵列可将三块[硬盘](../item/hdd1.md)组成一个文件系统，组合的文件系统拥有所有硬盘容量之和的大小，并且所有与其相连的电脑都能访问。

RAID 磁盘阵列需要三块硬盘方能工作并识别为一个文件系统。每块磁盘大小可以不同。

注意，当你往 RAID 加[硬盘](../item/hdd1.md)的时候，硬盘里的数据会全部丢失。而从 RAID 移除一块[硬盘](../item/hdd1.md)也会导致整个 RAID 数据丢失，加回去也没用，因为 RAID 会重新初始化。

然而，直接拆除 RAID 方块本身并不会丢失数据，所以这样可以安全移动数据。