<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="clickId" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clickId">
              <a-input v-model="model.clickId" placeholder="请输入clickId"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="playerId" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="playerId">
              <a-input v-model="model.playerId" placeholder="请输入playerId"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="offerId" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="offerId">
              <a-input v-model="model.offerId" placeholder="请输入offerId"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="clickIp" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clickIp">
              <a-input v-model="model.clickIp" placeholder="请输入clickIp"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="clickUa" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clickUa">
              <a-input v-model="model.clickUa" placeholder="请输入clickUa"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="click platform" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="click platform">
              <a-input v-model="model.click platform" placeholder="请输入click platform"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="click device" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="click device">
              <a-input v-model="model.click device" placeholder="请输入click device"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="clickOsVersion" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clickOsVersion">
              <a-input v-model="model.clickOsVersion" placeholder="请输入clickOsVersion"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1未完成、2完成" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input v-model="model.status" placeholder="请输入1未完成、2完成"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="clicktime" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clicktime">
              <j-date placeholder="请选择clicktime" v-model="model.clicktime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="0表示未删除,1表示删除" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="delFlag">
              <a-input v-model="model.delFlag" placeholder="请输入0表示未删除,1表示删除"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'WallOffersTasksForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/wallofferstasks/wallOffersTasks/add",
          edit: "/wallofferstasks/wallOffersTasks/edit",
          queryById: "/wallofferstasks/wallOffersTasks/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
    }
  }
</script>
