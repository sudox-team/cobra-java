#ifndef COBRA_JAVA_JDISCOVERY_HPP
#define COBRA_JAVA_JDISCOVERY_HPP

typedef struct discovery_data_t disc_data;
typedef struct discovery_data_finder_t finder_data;

struct discovery_data_t {
    jloader_data *loader_data;
    jobject ref;
    JNIEnv *env;
};

struct discovery_data_finder_t {
    jloader_data *loader_data;
    jobject ref;
    JNIEnv *env;
};

#endif
