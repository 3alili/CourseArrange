package com.pxw.pojo.GA;


import com.pxw.pojo.Task;

//个体类  候选解
//负责存储 操作一条染色体
//两个构造方法  1.接受长度 随机初始化一条染色体  2.接受数组 作为染色体
public class Individual {
    //染色体
    private int[] chromsome;
    //适应度追踪
    private double fitness = -1;


    public Individual(int[] chromsome) {
        //创建个体染色体
        this.chromsome = chromsome;
    }

    //初始化   利用timetable对象来确定必须排课的班级数(染色体长度)
    //染色体本身由timetable随机取得教室，时段，教授生成
    public Individual(Bootstrap bootstrap){
        //班级数
        int numTables = bootstrap.getNumTables();

        //1位基因为房间   1位基因为时间段
        int chromosomeLength = numTables*2;
        //创建随机的染色体
        int[] newChromosome = new int[chromosomeLength];
        int chromosomeIndex = 0;

        //教学任务循环
        for (Task task : bootstrap.getTasksAsArray()){
            // 定义可选的起始节次（两节连排和四节连排）
            int[] availableStartTimesTwo = {1, 3, 5, 7}; // 两节连排课程的起始节次
            int[] availableStartTimesFour = {1, 3, 5};  // 四节连排课程的起始节次

            int timeslotId = -1;

            // 根据课程类型选择合适的起始节次,A=体育，B=实验课，C=普通课程
//            System.out.println("task: " + task);
//            System.out.println("Cclasses: " + (task.getCclasses() != null ? task.getCclasses() : "null"));
//            System.out.println("Course: " + (task.getCclasses() != null ? task.getCclasses().getCourse() : "null"));

            if(task.getCclasses().getCourse().getRemark() == null){
                timeslotId = bootstrap.getRandomTimeslot().getId();
            } else if (task.getCclasses().getCourse().getRemark().equals("B")) {
                // 实验课程需要四节连排，选择四节连排的起始节次
                int startIndex = (int) (Math.random() * availableStartTimesFour.length);
                int startPeriod = availableStartTimesFour[startIndex];

                // 随机选择一个星期几（1-7），确保后续三节课程都在该星期的范围内
                int day = (int) (Math.random() * 5) + 1; // 星期几，范围 1-7

                // 计算四节连排课程的起始时间段
                if (startPeriod + 3 <= 8) { // 确保四节课在同一天内
                    timeslotId = (day - 1) * 8 + startPeriod; // 计算具体的时间段ID
                } else {
                    timeslotId = -1; // 如果无法选择有效的时间段，返回-1
                }
            } else if (task.getCclasses().getCourse().getRemark().equals("A")) {
                // 其他课程选择两节连排的起始节次
                int startIndex = (int) (Math.random() * availableStartTimesTwo.length);
                int startPeriod = availableStartTimesTwo[startIndex];

                // 随机选择一个星期几（1-7），确保后续一节课程在同一天内
                int day = (int) (Math.random() * 5) + 1; // 星期几，范围 1-7

                // 计算两节连排课程的起始时间段
                if (startPeriod + 1 <= 8) { // 确保两节课在同一天内
                    timeslotId = (day - 1) * 8 + startPeriod; // 计算具体的时间段ID
                } else {
                    timeslotId = -1; // 如果无法选择有效的时间段，返回-1
                }
            }

            newChromosome[chromosomeIndex] = timeslotId;
            chromosomeIndex++;

            //加入随机房间
            int roomId = bootstrap.getRandomRoom().getId();
            newChromosome[chromosomeIndex] = roomId;
            chromosomeIndex++;

//                //加入教学任务
//                //加入随机时间
//                int timeslotId = bootstrap.getRandomTimeslot().getId();
//                newChromosome[chromosomeIndex] = timeslotId;
//                chromosomeIndex++;
//
//                //加入随机房间
//                int roomId = bootstrap.getRandomRoom().getId();
//                newChromosome[chromosomeIndex] = roomId;
//                chromosomeIndex++;

        }
        this.chromsome = newChromosome;

    }


    public int[] getChromsome() {
        return chromsome;
    }

    public int getChromsomeLength() {
        return this.chromsome.length;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }


    //基因
    public int getGene(int offset) {
        return this.chromsome[offset];
    }

    public void setGene(int offset, int gene) {
        this.chromsome[offset] = gene;
    }

    //输出基因字符串
    @Override
    public String toString() {
        String output = "";
        for (int gene = 0; gene < this.chromsome.length; gene++) {
            output += this.chromsome[gene];
        }

        return output;
    }
}
