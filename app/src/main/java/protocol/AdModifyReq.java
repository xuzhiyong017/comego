// Code generated by Wire protocol buffer compiler, do not edit.
// Any Question Please Contact: jerryzhou@outlook.com
package protocol;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT32;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class AdModifyReq extends Message {

  public static final Integer DEFAULT_PAGE = 0;
  public static final Integer DEFAULT_ID = 0;
  public static final String DEFAULT_URL = "";
  public static final String DEFAULT_IMAGE = "";
  public static final String DEFAULT_FROMSTORE = "";

  @ProtoField(tag = 1, type = UINT32, label = REQUIRED)
  public final Integer page;

  /**
   * 所在页面
   */
  @ProtoField(tag = 2, type = UINT32)
  public final Integer id;

  @ProtoField(tag = 3, type = STRING)
  public final String url;

  @ProtoField(tag = 4, type = STRING)
  public final String image;

  @ProtoField(tag = 5, type = STRING)
  public final String FromStore;

  private AdModifyReq(Builder builder) {
    this.page = builder.page;
    this.id = builder.id;
    this.url = builder.url;
    this.image = builder.image;
    this.FromStore = builder.FromStore;
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof AdModifyReq)) return false;
    AdModifyReq o = (AdModifyReq) other;
    return equals(page, o.page)
        && equals(id, o.id)
        && equals(url, o.url)
        && equals(image, o.image)
        && equals(FromStore, o.FromStore);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = page != null ? page.hashCode() : 0;
      result = result * 37 + (id != null ? id.hashCode() : 0);
      result = result * 37 + (url != null ? url.hashCode() : 0);
      result = result * 37 + (image != null ? image.hashCode() : 0);
      result = result * 37 + (FromStore != null ? FromStore.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<AdModifyReq> {

    public Integer page;
    public Integer id;
    public String url;
    public String image;
    public String FromStore;

    public Builder() {
    }

    public Builder(AdModifyReq message) {
      super(message);
      if (message == null) return;
      this.page = message.page;
      this.id = message.id;
      this.url = message.url;
      this.image = message.image;
      this.FromStore = message.FromStore;
    }

    public Builder page(Integer page) {
      this.page = page;
      return this;
    }

    /**
     * 所在页面
     */
    public Builder id(Integer id) {
      this.id = id;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder image(String image) {
      this.image = image;
      return this;
    }

    public Builder FromStore(String FromStore) {
      this.FromStore = FromStore;
      return this;
    }

    @Override
    public AdModifyReq build() {
      checkRequiredFields();
      return new AdModifyReq(this);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }
}
